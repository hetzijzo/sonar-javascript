function bar(p1, p2){}

bar(1, 2, 3);  // Noncompliant [[sc=4;ec=13;el=+0;secondary=-2]] {{"bar" expects 2 arguments, but 3 were provided.}}
