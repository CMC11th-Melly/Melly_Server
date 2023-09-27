package cmc.mellyserver.mellybatch.config;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class QueueItemReader<T> implements ItemReader {

    private Queue<T> queue;

    public QueueItemReader(List<T> data) {
        this.queue = new LinkedList<>(data);
    }

    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return this.queue.poll();
    }
}
