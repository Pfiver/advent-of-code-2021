#!/bin/zsh

echo "curl https://adventofcode.com/2021/day/\[1-25] -o website/day\#1.html"

echo "run("
xmllint --html --xpath "//article/h2/text()" website/day*.html(n) 2> /dev/null |
    sort -nk3 |
    tr -d - |
    while IFS=: read day title
    do
      pkg=${(L)day//[^0-9A-Za-z]/}
      class=${title//[^0-9A-Za-z]/}

      cat <<-EOF >$pkg/$class.java
		package $pkg;

		import static common.IO.getInput;
		
		public class $class {

		    public static void main(String... args) {

		        // part 1
		        System.out.println(~0);

		        // part 2
		        System.out.println(~0);
		    }
		}
		EOF

	  echo "    , new Solution($pkg.$class.class, $pkg.$class::main)"
    done
echo ");"

echo "javac -d target/classes common/Run.java && java -cp target/classes common.Run"
