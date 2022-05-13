# filename_analysis

Downloading some Twitter images, I wanted to filter them and give some statistics over hashtags and account names.

And that was just an excuse to do some code :)

## What does it do ?

In short : *Parse filenames, Map the result and then Sort and Display.*

I wanted to do something that I can't do with a regular expression.
So I needed a small parser.

I'm using a list of delimiters/separators : by default _#|_@|. in the GUI where "|" is the separator of separators (so it cannot be used as a separator obviously).
I could have used basic one character separators but that was way too simple.

## Java

The main project is a Java + JavaFX code within a gradle project.
gradlew run.

The interesting part is the parser in the `StringSegmentation` class. But also with the way I'm sorting the result at the end of the `MainController.onChangeDirectory()` method.

Launch with : `./gradlew run` or `.\gradlew.bat run`

## Javascript - the fna_node submodule

I wanted to compare the code produced between langages.

Javascript has less code but can be tricky also.

The complexity resides in using the Json objects (produced by parsing) as maps and that I want to sort those maps but not maps by values.
I found something interesting on StackOverflow (URL is in the code commentaries).

Launch with : `npm run dev` but don't forget to change the `fileList` constant with a valid directory path.
