package handlers

import (
	"compress/gzip"
	"encoding/json"
	"github.com/zenazn/goji/web"
	"io"
	"log"
	"net/http"
	"strings"
)

type JsonHandlerResponse struct {
	// Http status code
	Status int

	// If status is 20*, this will be written in the "data" part of the JSON response.
	Data interface{}

	// Custom error code. Along with errorMessge, this will be writtein the "error" part of
	// the JSON response.
	ErrorCode    int
	ErrorMessage string
}

// Goji-compatible handler that automatically wraps responses into our standard
// JSON response format.
//
// This is implemented as a function that conforms to the goji.web.Handler inteface.
// See https://godoc.org/github.com/zenazn/goji/web#Handler.
// To use, make a handler that conforms to this type and then call:
//
//   HandlerFunc(yourHandler)
//
// The above converts the yourHandler to a HandlerFunc type.
//
// Borrowed a lot of ideas from Nap (https://github.com/crawford/nap)
type JsonHandlerFunc func(c web.C, rw http.ResponseWriter, r *http.Request) JsonHandlerResponse

// Calls self() and then wraps the response into the standard JSON response format
// and writes it to the ResponseWriter.
func (self JsonHandlerFunc) ServeHTTPC(c web.C, rw http.ResponseWriter, r *http.Request) {
	var finalRw http.ResponseWriter
	if strings.Contains(r.Header.Get("Accept-Encoding"), "gzip") {
		rw.Header().Set("Content-Encoding", "gzip")
		gzipWriter := gzip.NewWriter(rw)
		defer gzipWriter.Close()
		finalRw = gzipResponseWriter{Writer: gzipWriter, ResponseWriter: rw}
	} else {
		finalRw = rw
	}

	var response JsonHandlerResponse
	defer func() {
		finalRw.Header().Set("Content-Type", "application/json")
		finalRw.Header().Add("Cache-Control", "no-cache,must-revalidate")

		if panicErr := recover(); panicErr != nil {
			log.Println(panicErr)
			response.Status = http.StatusInternalServerError
		}
		output := make(map[string]interface{})
		if response.Status >= 200 && response.Status < 300 {
			output["data"] = response.Data
		} else {
			if len(response.ErrorMessage) == 0 {
				response.ErrorMessage = "An unexpected error has occurred."
			}
			output["error"] = map[string]interface{}{
				"code":    response.ErrorCode,
				"message": response.ErrorMessage,
			}
		}

		if json, err := json.Marshal(output); err == nil {
			finalRw.WriteHeader(response.Status)
			finalRw.Write(json)
		} else {
			// Change the status code to 500 if JSON marshaling fails.
			finalRw.WriteHeader(http.StatusInternalServerError)
		}
	}()
	response = self(c, finalRw, r)
}

// Empty implementation to conform to the goji.web.Handler interface.
func (f JsonHandlerFunc) ServeHTTP(rw http.ResponseWriter, r *http.Request) {
	// Noop
}

// Gzip writer used by the JsonHandlerFunc.
// https://gist.github.com/the42/1956518
type gzipResponseWriter struct {
	io.Writer
	http.ResponseWriter
}

func (w gzipResponseWriter) Write(b []byte) (int, error) {
	return w.Writer.Write(b)
}
