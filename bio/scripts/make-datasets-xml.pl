#!/usr/local/bin/perl
use strict;
use warnings;

use InterMine::Item::Document;
use InterMine::Model;

if (@ARGV < 2) {
    die "usage: $0 datasets_file model_file\n";
}

my ($datasets_file, $model_file) = @ARGV;
my %datasets = ();

my $model = new InterMine::Model(file => $model_file);
my $doc = new InterMine::Item::Document(model => $model);

open DATASETS, "<", $datasets_file or die "Error: unable to open file: $!\n";
while (<DATASETS>) {
    chomp;
    my @line = split /\t/;
    if ($line[0] eq "Publication") {
        $datasets{ $line[0] }{ $line[1] } = make_item($line[0] => (pubMedId => $line[1]));
    }
    else {
        $datasets{ $line[0] }{ $line[1] } = make_item(
            $line[0] => (
                name        => $line[1],
                description => $line[2],
                url         => $line[3],
            ),
        );
        if (defined $line[4]) {
            my @refs = split /,/, $line[4];
            foreach my $ref (@refs) {
                my ($refName, $refValue) = split /:/, $ref;
                my $lcRefName = lcfirst $refName;
                if ($line[0] eq "DataSource" and $refName eq "Publication") {
                    $datasets{ $line[0] }{ $line[1] }
                      ->set($lcRefName . "s" => [ $datasets{$refName}{$refValue} ]);
                }
                else {
                    $datasets{ $line[0] }{ $line[1] }
                      ->set($lcRefName => $datasets{$refName}{$refValue});
                }
            }
        }
    }
}
close DATASETS;

$doc->close();    # writes the xml
exit(0);

######### helper subroutines:

sub make_item {
    my @args = @_;
    my $item = $doc->add_item(@args);
    return $item;
}
