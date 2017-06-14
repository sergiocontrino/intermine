#!/bin/bash
#
# default usage: automine.sh
#
# note: you should put the db password in ~/.pgpass if don't
#       want to be prompted for it
#
# sc 09/08
#

URL="https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=taxonomy&term="
SPECIES=
#DIR="/home/contrino/data/rumen/genomes/"
DIR="/micklem/data/rumine/genomes/test/"
OUT="/micklem/data/rumine/genomes/out"
ERR=NOtaxid
IN=xproject

function printG {

L1="<source name=\""
L2="-gff\" type=\"gff\">"
L3="<property name=\"gff3.taxonId\" value=\""
L4="\"/>"
L5="<property name=\"gff3.seqDataSourceName\" value=\"Aber\"/>"
L6="<property name=\"gff3.dataSourceName\" value=\"Aber\"/>"
L7="<property name=\"gff3.seqClsName\" value=\"Chromosome\"/>"
L8="<property name=\"gff3.dataSetTitle\" value=\"prokka output\"/>"
L9="<property name=\"src.data.dir\" location=\""
LE="/></source>"

LH=$(echo "<!-- " $SPECIES " == " $TAXID " -->")

echo
echo $LH
echo $L1$SPECIES$L2
echo $L3$TAXID$L4
echo $L5
echo $L6
echo $L7
echo $L8
echo $L9$DIR$SPECIES$LE
}

function printF {
F1="<source name=\""
F2="-fasta\" type=\"fasta\">"
F3="<property name=\"fasta.taxonId\" value=\""
F4="\"/>"
F5="<property name=\"fasta.seqDataSourceName\" value=\"Aber\"/>"
F6="<property name=\"fasta.dataSourceName\" value=\"Aber\"/>"
F7="<property name=\"fasta.className\" value=\"org.intermine.model.bio.CDS\"/>"
F8="<property name=\"fasta.dataSetTitle\" value=\"prokka output\"/>"
FF="<property name=\"fasta.includes\" value=\"*.fna,*.faa\"/>"
F9="<property name=\"src.data.dir\" location=\""
FE="/></source>"

echo
echo $F1$SPECIES$F2
echo $F3$TAXID$F4
echo $F5
echo $F6
echo $F7
echo $F8
echo $FF
echo $F9$DIR$SPECIES$FE
}


function getName {
# rm prokka_ from the file name
SPECIES=`echo $1 | cut -c 8-`
#echo $SPECIES
}

function getTaxid {
wget -q $URL$SPECIES -O $SPECIES.xml

#TAXID=`grep -m1 '<Id>' $SPECIES.xml | cut -c 5- | cut -d\< -f1`

TAXID=$(grep -m1 '<Id>' $SPECIES.xml | cut -c 5- | cut -d\< -f1)

rm $SPECIES.xml
}


########################################
#
# MAIN
#
########################################

if [ -a $OUT/$ERR ]
then
rm $OUT/$ERR
fi

if [ -a $OUT/$IN ]
then
rm $OUT/$IN
fi

touch $OUT/$ERR $OUT/$IN

cd $DIR

for dir in *
do

getName $dir
getTaxid $SPECIES

echo $TAXID

if [ -n "$TAXID" ]
then
printG >> $OUT/$IN
printF >> $OUT/$IN
else
echo $SPECIES >> $OUT/$ERR
fi

done

