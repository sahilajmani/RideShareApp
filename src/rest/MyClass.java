package rest;

public class MyClass {

public boolean checkSyntax(String str){
int stack[]= new int[1000];
int top=-1;
int length = str.length();
int i,j;
boolean flag=true;
for(i=0;i<length;i++){
if(str.charAt(i)=='{'){
top++;
stack[top]=1;
}
else if(str.charAt(i)=='}'){
if(top>=0){
j=stack[top--];
if (j!=1){
flag=false;
break;
}else{
flag=false;
break;
}
}

}
}
return flag;


}
}
