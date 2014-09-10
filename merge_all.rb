#!/usr/bin/env ruby

require 'tmpdir'
require 'fileutils'
include FileUtils

path = ARGV.shift
out_path = ARGV.shift

if path.nil? or out_path.nil?
  puts "uso: #{$0} src_path dst_path"
  exit 1
end

Dir.mktmpdir do |tmp_dir|
  tmp_a = File.join(tmp_dir, 'a') 
  tmp_b = File.join(tmp_dir, 'b') 
  [tmp_a, tmp_b].each { |x| mkdir_p(x) }

  base = File.dirname(__FILE__)

  dirs = Dir.glob(File.join(path, '*'))
  dirs.delete_if { |x| !File.directory?(x) }

  cp_r(Dir.glob(File.join(dirs.first, '*')), tmp_a)
  dirs = dirs[1..-1]

  dirs.each do |path|
    next unless File.directory?(path)

    puts File.basename(path)
    out = `#{base}/merge_bones.rb "#{path}" "#{tmp_a}" "#{tmp_b}"`
    raise "error uniendo huesos" unless $?.success?

    rm_rf(tmp_a)
    mv(tmp_b, tmp_a)
  end

  rm_rf(out_path)
  mkdir_p(out_path)
  mv(Dir.glob(File.join(tmp_a, '*')), out_path)
end

