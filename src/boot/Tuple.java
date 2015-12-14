package boot;

class Tuple<Key, Value> 
{
  private final Key k;
  private  Value v;
   
  public Tuple(Key key, Value value) {
    k = key;
    v = value;
  }
 @Override
	public int hashCode() {
		return getKey().hashCode();
	}
 
  public String toString() {
    return String.format("%s, %s", getKey(), getValue());
  }
public Key getKey() {
	return k;
}
public Value getValue() {
	return v;
}
public void putValue(Value v){
	this.v=v;
}
   
}
