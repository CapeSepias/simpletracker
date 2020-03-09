#!/usr/bin/perl
use v5.10;
use strict;
use warnings FATAL => 'all';
use Time::Piece;

my %pageviews;
my $from;
my $to;

# (1) quit unless we have the correct number of command-line args
my $num_args = $#ARGV + 1;

if ($num_args == 2) {
    $from=$ARGV[0];
    $to=$ARGV[1];
    $from = Time::Piece->strptime($from, "%Y-%m-%dT%H:%M:%S");
    say "From: $from";
    $to = Time::Piece->strptime($to, "%Y-%m-%dT%H:%M:%S");
    say "To: $to";
    &ReadLogFile;
    &PrintResult;
} else {
    &PrintMan;
}

sub uniq {
    my %seen;
    grep !$seen{$_}++, @_;
}

sub PrintResult {
    say "url\t\t|page views\t|visitors";
    foreach my $page (sort keys %pageviews) {
        print "$page\t|";
        print scalar (@{ $pageviews{ $page } });
        print "\t\t|";
        print scalar (uniq @{ $pageviews{ $page } });
        print "\n";
    }
}

sub ReadLogFile {
    # Assume log file "/var/log/funnel/access.log"
    my $logfile = '/var/log/funnel/access.log';
    open(my $fh, '<:encoding(UTF-8)', $logfile,)
        or die "Could not open file '$logfile' $!";
    while (my $row = <$fh>) {
        chomp $row;
        $_ = $row;
        # Log file format
        # 2020-03-07 18:38:46.695 - /home.html - sIvXiLnV2eaRMOZg
        /^(\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}):\d{2}\.\d{3}\s-\s(\/[\w|\.]+)\s-\s(\w+)$/;
        my $logdatetime = Time::Piece->strptime("$1", "%Y-%m-%d %H:%M");
        if($from < $logdatetime && $to > $logdatetime){
            push @{ $pageviews{$2} }, $3;
        }
    }
}

sub PrintMan {
    say "\nUsage: 'perl logreader.pl from_date to_date'";
    say "Date format: yyyy-mm-ddThh:mm:ss";
    say "Use: 'perl logreader.pl test' for unit test. NOTE: This will overwrite any current /var/log/funnel/access.log";
    exit;
}



