$appName = "output-methods";
$tempName = "$appName" . "file";

require "getopts.pl";

&Getopts('j:h');

if ((!$opt_j) || $opt_h) {
    print "Usage: \n";
    print "$appName -j jarfile \n";
    print "\t For each java class in jarfile, for each method in that class\n";
    print "\t output the fully qualified method name to stdout.\n\n";

    exit(0);
}

#execute the jar -t command
open (JARLINES, "jar -tf $opt_j|");
@jarlines = <JARLINES>;
close(JARLINES);

$classes = "";


# we only care about .class files in the jar
@classlines = grep(/class$/, @jarlines);
foreach $_ (@classlines) {
  chop;
  # strip of the trailing .class
  $_ =~ s/.class//g;
  # change the "/" to "."
  $_ =~ s#/#.#g;
  # strip out anonymous inner classes
  if (!(/.*\$[0-9]/)) {
    # change the remaining "$" to "."
    $_ =~ s/\$/./g;
    # accrue
    $classes = $classes . " " . $_;
  }
}

open(JAVAPLINES, "javap -private -classpath $opt_j $classes |");
@javaplines = <JAVAPLINES>;
close(JAVAPLINES);
undef(@cache);

foreach $_ (@javaplines) {
  chop;
  # pull out the fqcn
  if (/{/) {
    @classline = split(' ', $_);
    for ($i = 0; $i < $#classline; $i++) {
      if ($classline[$i] eq "class") {
	$fqcn = $classline[$i + 1];
	break;
      }
      else {
	if ($classline[$i] eq "interface") {
	  # strip out a pesky "{"
	  @line = split("{", $classline[$i + 1]);
	  $fqcn = $line[0];
	  
	  break;
	}
      }
    }
  }

  # if the line contains "(", it is a method or a ctor
  if (/\(/) {
    # strip of the protection qualifier, if present
    @line = split(' ', $_);
    if ($line[0] eq "public" || $line[0] eq "private" || $line[0] eq "protected") {
      shift(@line);
      $_ = "@line";
    }
    # strip the static qualifier, if present
    @line = split(' ', $_);
    if ($line[0] eq "static"){
      shift(@line);
      $_ = "@line";
    }
    # strip the abstract or final, if present
    @line = split(' ', $_);
    if ($line[0] eq "abstract" || $line[0] eq "final"){
      shift(@line);
      $_ = "@line";
    }
    # strip the synchronized quantifier, if present
    if ($line[0] eq "synchronized") {
      shift(@line);
      $_ = "@line";
    }
    # strip the method arguments
    $_ =~ s/\(.*//g;
    @line = split(' ', $_);
    # if line is a ctor
    if (0 == $#line) {
      $_ = $fqcn . ".<init>";
    }
    else {
      # if the line is a method
      if (1 == $#line) {
	$_ = $fqcn . "." . $line[1];
      }
    }
     if (!(/\$/)) {
       # skip dups
      if (!$cache{$_}) {
	print "$_\n";
      }
      $cache{$_} = 1;
    }
  }
}
