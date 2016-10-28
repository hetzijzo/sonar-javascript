function main() {

  var a = foo();
  var b = bar();
  
  if (a > b) {
    let x = null;

    if (a < b) { // always false
      x = 0;
    }
    foo(x); // PS x=NULL
    
    if (a >= b) { // always true
      x = 0;
    }
    foo(x); // PS x=ZERO

    x = null
    if (a == b) {
      x = 0;
    }
    foo(x); // PS x=NULL
    
    x = null
    if (a === b) {
      x = 0;
    }
    foo(x); // PS x=NULL

  }
  
  if (a > unknown1) {
    let x2 = null;
    if (a > unknown2) {
      x2 = 0;
    }
    foo(x2); // PS x2=ZERO || x2=NULL
  }

  if (typeof a == "string" && typeof b == "number") {
    let x3 = null;

    if (a === b) {
      x3 = 0;
    }
    foo(x3); // PS x3=NULL

    if (a !== b) {
      x3 = 0;
    }
    foo(x3); // PS x3=ZERO
  }

  a = foo();
  b = bar();

  if (a === b) {
      if (a) {
        foo(); // PS b=TRUTHY
      }
  }

  makeLive(a, b);

}
