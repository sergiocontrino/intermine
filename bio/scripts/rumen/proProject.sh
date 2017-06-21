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
DDSED="\/home\/contrino\/data"

if [ "$HOST" != "$ABHOST" ]
then
DATADIR=/micklem/data/rumine     # test env in cam
DDSED="\/micklem\/data\/rumine"
fi

DGE="$DATADIR/genomes"
DUN="$DATADIR/uniprot"
DOU="$DATADIR/rumen"

ERR=NOtaxid
IN=proGenomes
UNI=xuniprot
STR=strains





function printG {
# printG species taxid
spec="$1"
id="$2"

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

LH=$(echo "<!-- " $spec " == " $id " -->")

echo
echo $LH
echo $L1$spec$L2
echo $LT$id$L4
echo $LS$spec$L4
echo $L5
echo $L6
echo $L7
echo $L8
echo $L9$DGE/prokka_$spec$L4$LE
}

function printF {
# printF species taxid
  spec="$1"
  id="$2"

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
echo $F1$spec$F2
echo $FT$id$F4
echo $FS$spec$F4
echo $F5
echo $F6
echo $F7
echo $F8
echo $FF
echo $F9$DGE/prokka_$spec$F4$FE
}


function writeUniprot {
LIST=$(cat $OUT/uni.taxid | tr '\n' ','| head -c -1)

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
}

function getTaxid {
  # getTaxid species
wget -q $TAXURL$1 -O $1.xml

TAXID=$(grep -m1 '<Id>' $1.xml | cut -c 5- | cut -d\< -f1)

rm $1.xml
}

function setFiles {

DATE=$(date "+%y%m%d.%H%M")

DLOG=$DOU/logs  # unused

mkdir $DOU/$DATE
OUT="$DOU/$DATE"

if [ -a $OUT/$ERR ]
then
rm $OUT/$ERR
fi

if [ -a $OUT/$IN ]
then
rm $OUT/$IN
fi

if [ -a $OUT/$UNI ]
then
rm $OUT/$UNI
fi

if [ -a $OUT/$STR ]
then
rm $OUT/$STR
fi

#
touch $OUT/$ERR $OUT/$IN $OUT/$UNI $OUT/$STR

}


function doProject {
SCRIPTDIR=$MINEDIR/../bio/scripts/rumen

sed "s/LOCATION/$DDSED/g" $SCRIPTDIR/proStart > $OUT/p1

cat $OUT/p1 $OUT/proUniprot $OUT/proGenomes $SCRIPTDIR/proEnd > $OUT/project.xml

rm $OUT/p1

}

########################################
#
# MAIN
#
########################################

setFiles


# first round
cd $DGE


for dir in *
do

getName $dir
getTaxid $SPECIES

if [ -n "$TAXID" ]
then
printG $SPECIES $TAXID >> $OUT/$IN
printF $SPECIES $TAXID >> $OUT/$IN
echo $TAXID
echo $TAXID >> $OUT/$UNI
else
#  echo $SPECIES
echo $SPECIES >> $OUT/$STR
SPE=$(echo $SPECIES | rev | cut -d'_' -f1 --complement | rev)
getTaxid $SPE
echo $SPECIES" -> "$SPE":"$TAXID

if [ -n "$TAXID" ]
then
printG $SPECIES $TAXID >> $OUT/$IN
printF $SPECIES $TAXID >> $OUT/$IN
echo $TAXID >> $OUT/$UNI
else
echo $SPECIES >> $OUT/$ERR
fi

fi

done


sort -u $OUT/$UNI > $OUT/uni.taxid

cd $MINEDIR
./autoget -v -f $OUT/uni.taxid

writeUniprot > $OUT/proUniprot

doProject

if [ -a $OUT/project.xml ]
then
mv $OUT/project.xml $MINEDIR
fi
