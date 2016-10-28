/*function sayHello(c) {
  if (c) {
    foo();
  }
  if (a == b) {     // Noncompliant {{Replace "==" with "===".}}
//      ^^
  }
  if (a != b) {     // Noncompliant {{Replace "!=" with "!==".}}
  }
  if (a === b) {    // OK
  }
  if (a !== b) {    // OK
  }
  if (a != null) {  // OK
  }
  if (a == null) {  // OK
  }
  if (null == a) {  // OK
  }
  if (null == null) {  // OK
  }
  foo(c);
}

function withTypes(a, b) {
  if (typeof a == "string" && typeof b == "string") { // OK x 2

    if (a == b) {     // OK
    }
    if (a != b) {     // OK
    }
    if (a === b) {    // OK
    }
    if (a !== b) {    // OK
    }
    if (a != null) {  // OK
    }
    if (a == null) {  // OK
    }
  }

  if (typeof a == "string" && typeof b == "number") {
    if (a == b) {     // Noncompliant
    }
    if (a === b) {    // OK
    }
  }

  if (typeof a == "string") {
    if (a == b) {     // Noncompliant
    }
    if (a === b) {    // OK
    }
  }

}

if (a == b) { // Noncompliant
}

function withTry() {
  try {
    foo();
  } catch(e) {}

  if (a == b) { // Noncompliant
  }
}
*/

function jjj(x1, y1, x2, y2) {

      if (x1 === x2 && y1 === y2) {
        return
      }

      foo(x1 || 0, y1 || 0);
      foo(x2 || 0, y2 || 0);

      if (drawCrisp) {
        if (x1 === x2) curContext.translate(-0.5, 0);
      }
    }
