package lexer;
/*
 * �����������࣬������Token��������һ�����ݳ�Աvalue�����أ�ע���������ͣ���
 * ���Ĺ��캯�������û��๹�캯����ʼ��tagֵ�󣬻���value����ֵ��
 */
public class Num extends Token{  
    public final int value;  
    
    public Num(int v) {  
        super(Tag.NUM);  
        this.value = v;  
    }  
    public String toString() {  
        return  "num, " + value;  
    }  
}  