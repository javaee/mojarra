$appName = "generate-savebegin";
$tempName = "$appName" . "file";

require "getopts.pl";

&Getopts('o:j:h');

if ((!$opt_j) || (!$opt_o) || $opt_h) {
    print "Usage: \n";
    print "$appName -o outputMethods-script -j jarfile \n";
    print "\t For each method in each class in jarfile, generate \n";
    print "\t JCov -savebegin=.  Use outputMethods-script to do so.\n\n";

    exit(0);
}

open(METHODS, "perl $opt_o -j $opt_j|");
@methods = <METHODS>;
close(METHODS);
$line = "";

foreach $_ (@methods) {
  chop;
  if (/</) {
    $line = "$line" . "\"-savebegin=$_\" ";
  }
  else {
    $line = "$line" . "-savebegin=$_ ";
  }
}

print "jcov.savepoints=$line\n";
