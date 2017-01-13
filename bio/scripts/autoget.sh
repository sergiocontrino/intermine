#!/bin/bash
#
# default usage: automine.sh
#
# sc 09/08
#
#

#
# check the host
#
ABHOST="rumenmine-dev"
DATADIR=/home/contrino/data   # default datadir (on rumenmine-dev)

HOST=`hostname`
#echo $HOST

if [ "$HOST" != "$ABHOST" ]
then
DATADIR=/micklem/data/rumine/data
fi
#echo $DATADIR

SRCDIR=$DATADIR/uniprot
LOGDIR=$SRCDIR/logs

FTPURL=http://www.uniprot.org/uniprot

PROPDIR=$HOME/.intermine
SCRIPTDIR=./scripts

ARKDIR=/micklem/releases/modmine

RECIPIENTS=contrino@intermine.org

# set minedir and check that modmine in path
MINEDIR=$PWD
BUILDDIR=$MINEDIR/integrate/build


# default settings: edit with care
V=nv             # non-verbose mode
INFILE=          # not using a given list of submissions
INTERACT=n       # y: step by step interaction
WGET=y           # use wget to get files from ftp
DB=a             # no db specified (do them all)


progname=$0

function usage () {
  cat <<EOF

Usage:
$progname [-f file_name] [-i] [-v] [-s] [-t] taxId
  -f file_name: using a given list of submissions
  -i: interactive mode
  -v: verbode mode


Parameters: you can process
            a single organism (taxId)                    (e.g. automine.sh 9913 )
            a list of organisms (taxId) in an input file (e.g. automine.sh -V -f infile )

examples:

EOF
  exit 0
}


while getopts ":if:vst" opt; do
  case $opt in

  f )  INFILE=$OPTARG;;
  i )  echo "- Interactive mode" ; INTERACT=y;;
  v )  echo "- Verbose mode" ; V=v;;
  s )  echo "- Only Swiss-Prot" ; DB=s;;
  t )  echo "- Only TrEMBL" ; DB=t;;
  h )  usage ;;
  \?)  usage ;;
  esac
done

shift $(($OPTIND - 1))

# some input checking

if [ -n "$INFILE" ]
then
if [ ! -s "$INFILE" ]
then
echo "ERROR, $INFILE: no such file?"
echo
exit 1;
fi
SHOW="`cat $INFILE|tr '[\n]' '[,]'`"; echo -n "- Using given list of taxids: "; echo $SHOW;
fi


echo "==================================="
echo "GETTING UNIPROT FILES "
echo "==================================="
echo


if [ -n "$1" ]
then
SUB=$1
#echo "Processing taxon $SUB.."
fi

function interact {
# if testing, wait here before continuing
if [ $INTERACT = "y" ]
then
echo "$1"
echo "Press return to continue (^C to exit).."
echo -n "->"
read
fi

}


function getFiles {
#---------------------------------------
# getting the xml from ftp site
#---------------------------------------

if [ -n "$SUB" ]
then
# doing only 1 sub
LOOPVAR="$SUB"
elif [ -n "$INFILE" ]
then
# use the list provided in a file
LOOPVAR=`cat $INFILE`
else
echo "ERROR: please enter input file location."
fi

cd $SRCDIR

#interact "START WGET NOW"

for sub in $LOOPVAR
do
echo "Processing taxon $sub.."
if [ "$DB" = "a" -o "$DB" = "s" ]
then
wget -O $sub\_uniprot_sprot.xml -$V  --no-use-server-timestamps "http://www.uniprot.org/uniprot/?compress=no&query=organism:$sub%20AND%20reviewed:yes&fil=&format=xml"
fi

if [ "$DB" = "a" -o "$DB" = "t" ]
then
wget -O $sub\_uniprot_trembl.xml -$V --progress=dot:mega --no-use-server-timestamps "http://www.uniprot.org/uniprot/?compress=no&query=organism:$sub%20AND%20reviewed:no&fil=&format=xml"
fi

done

}

interact

########################################
#
# MAIN
#
########################################

#---------------------------------------
# get the xml files
#---------------------------------------
#
if [ "$WGET" = "y" ]
then
getFiles
echo bye!
#interact
fi #if $WGET=y
