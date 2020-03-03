package com.pjqdyd.writable;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**   
 * @Description:  [自定义数据类型]
 * 实现Writable 不能排序
 * 实现WritableComparable 可排序
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class MyWritable implements WritableComparable<MyWritable> {
    public String word;
    public int counter;

    public MyWritable(){}
    public MyWritable(String word, int counter) {
        this.word = word;
        this.counter = counter;
    }

    //比较器, 决定排序方式
    public int compareTo(MyWritable myWritable) {
        return myWritable.counter - this.counter; //倒序排列
    }

    //序列化
    public void readFields(DataInput dataInput) throws IOException {
        this.word = dataInput.readUTF();
        this.counter = dataInput.readInt();
    }

    //反序列化
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.word);
        dataOutput.writeInt(this.counter);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "[" + word + "\t" + counter + "]";
    }
}
