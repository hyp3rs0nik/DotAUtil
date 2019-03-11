# config valid only for Capistrano 3.1
lock '3.2.1'

set :application, 'mydota'
set :repo_url, 'git@bitbucket.org:rbesiera/dotautil.git'

set :scm, :git
#set :git_strategy, SubmoduleStrategy
set :branch, 'master'
set :deploy_to, '/var/apps/mydota'
set :go_path, "#{deploy_to}/gopath"

set :keep_releases, 5

set :ssh_options, {
  keys: [ENV['SSH_KEY']],
  keys_only: true,
  forward_agent: true
}

set :format, :pretty
set :log_level, :debug

namespace :deploy do
  task :build do 
    on roles(:all) do
      # Copy latest deploy.sh
      execute "cp -f #{release_path}/web/remote-bin/build.sh #{deploy_to}/bin/build.sh"
      execute "chmod +x #{deploy_to}/bin/build.sh"
      output = capture("#{deploy_to}/bin/build.sh")
      output.each_line do |line|
        puts line 
      end
    end
  end

  task :migrate_db do 
    on roles(:dataserver) do 
      goose = "#{fetch(:go_path)}/bin/goose"
      execute "#{goose} -env=live -path=#{release_path}/web/gopath/src/hamak/mydota/goose up"
    end
  end

  task :restart_daemons do
    on roles(:all) do
      execute "sudo supervisorctl restart mydota"
    end
  end

  # Flow doc: http://capistranorb.com/documentation/getting-started/flow/
  after :published, :build
  after :published, :migrate_db
  after :published, :restart_daemons
end

