package com.example.springbatchexample.db2file;

import com.example.springbatchexample.Customer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class TextFileCustomerItemWriter implements ItemWriter<Customer> {
    private String filePath;

    public TextFileCustomerItemWriter(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void write(Chunk<? extends Customer> items) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Customer item : items) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
