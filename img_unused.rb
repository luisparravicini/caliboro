#!/usr/bin/env ruby

require 'json'
require 'fileutils'

#
# Lista las imagenes de cierto path que no estan siendo referenciadas por Caliboro
#

dir = ARGV.shift
remove = (ARGV.shift == "-r")
if dir.nil?
  puts "usage: #{$0} <path> [-r]"
  exit 1
end


def used_files(path)
  json = JSON.load(IO.read(File.join(path, 'bones.json')))

  json['bones'].map { |x| x['images'] }.map do |img|
    img.map { |a| a['imagePath'] }
  end.flatten
end


Dir.glob(File.join(dir, '*')).each do |path|
  next unless File.directory?(path)

  puts "-"*60
  puts path
  used = used_files(path)
  Dir.glob(File.join(path, '*')).each do |subpath|
    name = File.basename(subpath)
    next unless name =~ /\.jpg$/i

    unless used.include?(name)
      puts subpath
      FileUtils.rm(subpath)
    end
  end


  used.each do |img_path|
    raise "#{img_path} not found" unless File.exist?(File.join(path, img_path))
  end

end

