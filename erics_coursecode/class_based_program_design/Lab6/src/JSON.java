interface JSON {
  <R> R accept(JSONVisitor<R> visitor);
}

// no value
class JSONBlank implements JSON {
  // apply vs accept
  public <R> R accept(JSONVisitor<R> visitor) {
    return visitor.visitJSONBlank(this);
  }
}
 
// a number
class JSONNumber implements JSON {
  int number;
 
  JSONNumber(int number) {
    this.number = number;
  }
  
  public <R> R accept(JSONVisitor<R> visitor) {
    return visitor.visitJSONNumber(this);
  }
}
 
// a boolean
class JSONBool implements JSON {
  boolean bool;
 
  JSONBool(boolean bool) {
    this.bool = bool;
  }
  
  public <R> R accept(JSONVisitor<R> visitor) {
    return visitor.visitJSONBool(this);
  }
}
 
// a string
class JSONString implements JSON {
  String str;
 
  JSONString(String str) {
    this.str = str;
  }

  public <R> R accept(JSONVisitor<R> visitor) {
    return visitor.visitJSONString(this);
  }
}

/*
  Implement the JSONVistor<T> interface, which is a IFunc<JSON, T> and follows the visitor pattern over JSONs.
  Define a JSONToNumber visitor, which coverts a JSON to its number value. 
  Blanks are converted to 0, booleans 0 or 1 depending on 
  if the value is false or true, strings their length, and numbers their value.
  Map over a list of JSON and produce all of their numbers as a test.
*/
interface JSONVisitor<T> extends IFunc<JSON,T> {
  public T apply(JSON input);
  public T visitJSONBlank(JSONBlank jsonBlank);
  public T visitJSONNumber(JSONNumber jsonNum);
  public T visitJSONBool(JSONBool jsonBool);
  public T visitJSONString(JSONString jsonStr);
}

class JSONToNumber implements JSONVisitor<Integer> {
  public Integer apply(JSON input) {
    return input.accept(this);
  }
  
  public Integer visitJSONBlank(JSONBlank jsonBlank) {
    return 0;
  }

  public Integer visitJSONNumber(JSONNumber jsonNum) {
    return jsonNum.number;
  }
  
  public Integer visitJSONBool(JSONBool jsonBool) {
    if (jsonBool.bool) {
      return 1;
    } else {
      return 0;
    }
  }

  public Integer visitJSONString(JSONString jsonStr) {
    return jsonStr.str.length();
  }
}
