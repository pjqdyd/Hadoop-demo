package com.pjqdyd.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**   
 * @Description:  [二次排序的数据类型]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class SecondarySortWritable implements WritableComparable<SecondarySortWritable> {

    public int first; //第一个数
    public int second; //第二个数

    public SecondarySortWritable(){}
    public SecondarySortWritable(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public void readFields(DataInput dataInput) throws IOException {
        this.first = dataInput.readInt();
        this.second = dataInput.readInt();
    }

    public int compareTo(SecondarySortWritable secondarySortWritable) {
        int tmp = 0;
        tmp = this.first - secondarySortWritable.first; //升序
        if (tmp != 0){
            return tmp; //比较第一个数
        }
        //如果第一个数相同比较第二个数
        return this.second - secondarySortWritable.second; //升序
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.first);
        dataOutput.writeInt(this.second);
    }

    @Override
    public String toString() {
        return "SecondarySortWritable{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
