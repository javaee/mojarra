$appName = "generate-savebegin";
$tempName = "$appName" . "file";

require "getopts.pl";

&Getopts('o:j:lh');

if ((!$opt_j) || (!$opt_o) || $opt_h) {
    print "Usage: \n";
    print "$appName -o outputMethods-script -j jarfile [-l]\n";
    print "\t For each method in each class in jarfile, generate \n";
    print "\t JCov -savebegin=.  Use outputMethods-script to do so.\n";
    print "\t options:\n";
    print "\t          -l means output each option on a line by itself.\n\n";

    exit(0);
}

open(METHODS, "perl $opt_o -j $opt_j|");
@methods = <METHODS>;
close(METHODS);
$line = "";

foreach $_ (@methods) {
  if ($opt_l) {
    print "-savebegin=$_";
  }
  else {
    chop;
    if (/</) {
      $line = "$line" . "\"-savebegin=$_\" ";
    }
    else {
      $line = "$line" . "-savebegin=$_ ";
    }
  }
}

if (!($opt_l)) {
  print "jcov.savepoints=$line\n";
}
