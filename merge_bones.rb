#!/usr/bin/env ruby

require 'json'
require 'fileutils'
include FileUtils


path_a = ARGV.shift
path_b = ARGV.shift
$out_path = ARGV.shift

if path_a.nil? || path_b.nil? || $out_path.nil?
	puts "usage: #{$0} <path_a> <path_b> <out_path>"
	exit 1
end

def read_json(path)
	doc = JSON.load(IO.read(File.join(path, 'bones.json')))
end

$bone_count = 0
def copy_bones(base_path, doc)
	doc['bones'].tap do |bones|
		bones.each do |bone|
			p bone['name']
			bone['images'].each do |img|
				image_file = File.join(base_path, img['imagePath'])
				puts "\t#{img['name']}"
				ext = File.extname(image_file).downcase
				final_name = '%d%s' % [$bone_count, ext]
				cp(image_file, File.join($out_path, final_name))
				img['imagePath'] = final_name

				$bone_count += 1
			end
		end
	end
end

def write_json(doc)
	doc = { 'bones' => doc, 'lastImageId' => $bone_count + 1 }
	path = File.join($out_path, 'bones.json')
	File.open(path, 'w') { |io| io.write(JSON.dump(doc)) }
end



rm_rf($out_path)
mkdir_p($out_path)

bones_a = read_json(path_a)
bones_b = read_json(path_b)

doc = []
doc << copy_bones(path_a, bones_a)
doc << copy_bones(path_b, bones_b)
write_json(doc)

