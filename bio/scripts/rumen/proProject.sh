#!/bin/bash
#
# default usage: automine.sh
#
# note: you should put the db password in ~/.pgpass if don't
#       want to be prompted for it
#
# sc 09/08
#
HOST=`hostname -s`
TAXURL="https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=taxonomy&term="
MINEDIR=$PWD
SPECIES=
DATADIR="/home/contrino/data"    # default in aber

if [ "$HOST" != "$ABHOST" ]
then
DATADIR=/micklem/data/rumine     # test env in cam
fi

DGE="$DATADIR/genomes"
DUN="$DATADIR/uniprot"
DOU="$DATADIR/rumen"

ERR=NOtaxid
IN=proGenomes
UNI=xuniprot

DLOG=$DOU/logs  # unused

function printG {

L1="<source name=\""
L2="-gff\" type=\"gff\">"
LT="<property name=\"gff3.taxonId\" value=\""
L4="\"/>"
LS="<property name=\"gff3.name\" value=\""
L5="<property name=\"gff3.seqDataSourceName\" value=\"Aber\"/>"
L6="<property name=\"gff3.dataSourceName\" value=\"Aber\"/>"
L7="<property name=\"gff3.seqClsName\" value=\"Chromosome\"/>"
L8="<property name=\"gff3.dataSetTitle\" value=\"prokka output\"/>"
L9="<property name=\"src.data.dir\" location=\""
LE="</source>"

LH=$(echo "<!-- " $SPECIES " == " $TAXID " -->")

echo
echo $LH
echo $L1$SPECIES$L2
echo $LT$TAXID$L4
echo $LS$SPECIES$L4
echo $L5
echo $L6
echo $L7
echo $L8
echo $L9$DGE/prokka_$SPECIES$L4$LE
}

function printF {
F1="<source name=\""
F2="-fasta\" type=\"fasta\">"
FT="<property name=\"fasta.taxonId\" value=\""
FS="<property name=\"fasta.name\" value=\""
F4="\"/>"
F5="<property name=\"fasta.seqDataSourceName\" value=\"Aber\"/>"
F6="<property name=\"fasta.dataSourceName\" value=\"Aber\"/>"
F7="<property name=\"fasta.className\" value=\"org.intermine.model.bio.CDS\"/>"
F8="<property name=\"fasta.dataSetTitle\" value=\"prokka output\"/>"
FF="<property name=\"fasta.includes\" value=\"*.fna\"/>"
F9="<property name=\"src.data.dir\" location=\""
FE="</source>"

echo
echo $F1$SPECIES$F2
echo $FT$TAXID$F4
echo $FS$SPECIES$F4
echo $F5
echo $F6
echo $F7
echo $F8
echo $FF
echo $F9$DGE/prokka_$SPECIES$F4$FE
}

function doUniprotList {
LIST=$(cat $1 | tr '\n' ','| head -c -1)
#echo $LIST
}

function writeUniprot {
doUniprotList $DOU/$UNI

#echo $LIST

U1="<source name=\"uniprot-rumen\" type=\"uniprot\">"
U2="<property name=\"uniprot.organisms\" value=\""
U3="\"/>"
U4="<property name=\"creatego\" value=\"true\"/>"
U5="<property name=\"src.data.dir\" location=\""
U6="</source>"

echo $U1
echo $U2$LIST$U3
echo $U4
echo $U5$DUN/$U3$U6
}


function getName {
# rm prokka_ from the file name
SPECIES=`echo $1 | cut -c 8-`
#echo $SPECIES
}

function getTaxid {
wget -q $TAXURL$SPECIES -O $SPECIES.xml

#TAXID=`grep -m1 '<Id>' $SPECIES.xml | cut -c 5- | cut -d\< -f1`

TAXID=$(grep -m1 '<Id>' $SPECIES.xml | cut -c 5- | cut -d\< -f1)

rm $SPECIES.xml
}

function setFiles {

if [ -a $DOU/$ERR ]
then
rm $DOU/$ERR
fi

if [ -a $DOU/$IN ]
then
rm $DOU/$IN
fi

if [ -a $DOU/$UNI ]
then
rm $DOU/$UNI
fi

touch $DOU/$ERR $DOU/$IN $DOU/$UNI

}


function doProject {

cat $DOU/proStart $DOU/proUniprot $DOU/proGenomes $DOU/proEnd > $DOU/project.xml

}

########################################
#
# MAIN
#
########################################

setFiles

cd $DGE

for dir in *
do

getName $dir
getTaxid $SPECIES

echo $TAXID

if [ -n "$TAXID" ]
then
printG >> $DOU/$IN
printF >> $DOU/$IN
echo $TAXID >> $DOU/$UNI
else
echo $SPECIES >> $DOU/$ERR
fi

done

cd $MINEDIR
./autoget -v -f $DOU/$UNI

writeUniprot > $DOU/proUniprot

doProject

if [ -a $DOU/project.xml ]
then
mv $DOU/project.xml $MINEDIR
fi
