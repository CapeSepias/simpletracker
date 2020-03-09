#!/usr/bin/perl
use v5.10;
use strict;
use warnings FATAL => 'all';

#Run unit test. Overwrite any current /var/log/funnel/access.log with known data
#and assure that the correct number of visits are found. The written access.log
#will contain one record that is too old and one that is too new.

open(testLog, ">/var/log/funnel/access.log");
say testLog "2020-03-07 14:38:46.695 - /home.html - sIvXiLnV2eaRMOZg";
say testLog "2020-03-07 18:46:10.767 - /home.html - mL4FDaj267ldVFpY";
say testLog "2020-03-07 18:46:37.533 - /home.html - mL4FDaj267ldVFpY";
say testLog "2020-03-07 18:46:53.695 - /home.html - fCZZtURlYDG2Oeq2";
say testLog "2020-03-07 19:47:37.173 - /home.html - GmVCGSWtFYLPD1hn";
say testLog "2020-03-07 19:47:37.263 - /home.html - mL4FDaj267ldVFpY";
say testLog "2020-03-07 20:55:36.932 - /home.html - RDfZDMyLjiLINLzw";
say testLog "2020-03-08 12:25:28.780 - /about.html - oJOg0wx9phWY6dS5";
say testLog "2020-03-08 12:25:40.467 - /contact.html - mL4FDaj267ldVFpY";
say testLog "2020-03-08 12:25:42.467 - /contact.html - mL4FDaj267ldVFpY";
say testLog "2020-03-08 14:25:48.834 - /home.html - oJOg0wx9phWY6dS5";
say testLog "2020-03-08 14:25:54.254 - /contact.html - oJOg0wx9phWY6dS5";
say testLog "2020-03-08 15:26:13.973 - /contact.html - g9QN4AkKUUfijlbU";
say testLog "2020-03-08 15:36:18.375 - /home.html - g9QN4AkKUUfijlbU";
close (testLog);

my $aboutVisitors;
my $aboutUnique;
my $contactVisitors;
my $contactUnique;
my $homeVisitors;
my $homeUnique;

# This span should return for the above test log
#url             |page views     |visitors
#/about.html     |1              |1
#/contact.html   |3              |3
#/home.html      |7              |5
my $goodResult = `perl logreader.pl 2020-03-07T18:00:00 2020-03-08T15:30:00`;
foreach (split(/\n/, $goodResult)) {
    next if not /^\/\w+/;
    $aboutVisitors = $1 if /^\/about\.html\t\|(\d)\t\t\|(\d)/;
    $aboutUnique = $2 if /^\/about\.html\t\|(\d)\t\t\|(\d)/;
    $contactVisitors = $1 if /^\/contact\.html\t\|(\d)\t\t\|(\d)/;
    $contactUnique = $2 if /^\/contact\.html\t\|(\d)\t\t\|(\d)/;
    $homeVisitors = $1 if /^\/home\.html\t\|(\d)\t\t\|(\d)/;
    $homeUnique = $2 if /^\/home\.html\t\|(\d)\t\t\|(\d)/;
}
if($aboutVisitors == 1 && $aboutUnique == 1 && $contactVisitors == 4 && $contactUnique == 3 && $homeVisitors == 7 && $homeUnique == 5){
    say "Correct visitors for good result found";
    say "Test passed";
} else {
    say "Incorrect visitors for good result found";
    say "Test NOT passed";
}

#This span should return more visits than checked for and thus test will fail if correct number of visits is found.
my $badResult = `perl logreader.pl 2010-03-07T18:00:00 2030-03-08T15:30:00`;
foreach (split(/\n/, $badResult)) {
    next if not /^\/\w+/;
    $aboutVisitors = $1 if /^\/about\.html\t\|(\d)\t\t\|(\d)/;
    $aboutUnique = $2 if /^\/about\.html\t\|(\d)\t\t\|(\d)/;
    $contactVisitors = $1 if /^\/contact\.html\t\|(\d)\t\t\|(\d)/;
    $contactUnique = $2 if /^\/contact\.html\t\|(\d)\t\t\|(\d)/;
    $homeVisitors = $1 if /^\/home\.html\t\|(\d)\t\t\|(\d)/;
    $homeUnique = $2 if /^\/home\.html\t\|(\d)\t\t\|(\d)/;
}
if($aboutVisitors == 1 && $aboutUnique == 1 && $contactVisitors == 4 && $contactUnique == 3 && $homeVisitors == 7 && $homeUnique == 5){
    say "Incorrect visitors for bad result found";
    say "Test NOT passed";
} else {
    say "Incorrect visitors for bad result found";
    say "Test passed";
}

