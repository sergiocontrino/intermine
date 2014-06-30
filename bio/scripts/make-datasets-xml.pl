#!/usr/local/bin/perl
use strict;
use warnings;

use InterMine::Item::Document;
use InterMine::Model;

if (@ARGV < 2) {
    die "usage: $0 datasets_file model_file\n";
}

my ($datasets_file, $model_file) = @ARGV;
my %data = ();

my $model = new InterMine::Model(file => $model_file);
my $doc = new InterMine::Item::Document(model => $model);

open DATASETS, "<", $datasets_file or die "Error: unable to open file: $!\n";
while (<DATASETS>) {
    chomp;
    my @line = split /\t/;

    # process all the publications first
    my @refs = ();
    if (defined $line[4]) {
        @refs = split /,/, $line[4];
        foreach my $ref (@refs) {
            my ($refName, $refValue) = split /:/, $ref;
            next unless ($refName eq "Publication");
            $data{$refName}{$refValue} = make_item($refName => (pubMedId => $refValue))
              if (not defined $data{$refName}{$refValue});
        }
    }

    # process the datasource/set
    $data{ $line[0] }{ $line[1] } = make_item(
        $line[0] => (
            name        => $line[1],
            description => $line[2],
            url         => $line[3],
        ),
    );

    # set all the references/collections
    foreach my $ref (@refs) {
        my ($refName, $refValue) = split /:/, $ref;
        my ($lcRefName, $refId) = (lcfirst $refName, $data{$refName}{$refValue});
        if ($line[0] eq "DataSource" and $refName eq "Publication") {
            $lcRefName .= "s";
            $refId = [ $refId ];
        }
        $data{ $line[0] }{ $line[1] }->set($lcRefName => $refId);
    }
}
close DATASETS;

$doc->close();    # write the xml
exit(0);

######### helper subroutines:

sub make_item {
    my @args = @_;
    my $item = $doc->add_item(@args);
    return $item;
}
