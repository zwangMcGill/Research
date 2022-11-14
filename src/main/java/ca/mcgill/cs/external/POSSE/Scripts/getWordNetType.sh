#! /bin/tcsh
../Dictionaries/wordnet/WordNet-3.0 $1 | grep "Information available for \(noun\|verb\|adj\|adv\) $1" | cut -d " " -f4
