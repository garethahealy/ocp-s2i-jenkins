[![Build Status](https://travis-ci.org/garethahealy/jenkins-plugin-generator-lib.svg?branch=master)](https://travis-ci.org/garethahealy/jenkins-plugin-generator-lib)
[![License](https://img.shields.io/hexpm/l/plug.svg?maxAge=2592000)]()

# jenkins-plugin-generator-lib
Jenkins global groovy library which generates a Jenkins plugins.txt that can be used in a container image build.
- https://jenkins.io/doc/book/pipeline/shared-libraries/

## what does it do?
Generates a plugins.txt with fixed versions

## how does it do it?
You provide a plugins.txt.template which names what plugins you want.
If no version is included, the latest is used.
If a version is included, then it is regarded as 'pinned' and that version is used.
All plugins and their dependencies will be included in the final plugins.txt

Pinning versions which are very old (i.e.: 6 months+) may cause problems due to other plugins requiring a newer version.

## how do i use it?
See:
- https://github.com/garethahealy/jenkins-master-s2i