package org.example.nocopy;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

class FileChannelReader {
    private static final int MIN_BUFFERSIZE = 102400;
    private static final int REMAINING_MIN_BUFFERSIZE = 200;

    private final FileChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(MIN_BUFFERSIZE);
    private final byte[] data = buffer.array();
    private boolean EOF = false;
    private boolean hasNewLine = true;
    private int pos = 0;
    private int end = 0;
    private int lineStartPos = 0;
    private int semicolonPos = -1;
    private int newlinePos = -1;
    private int hashCode = -1;

    FileChannelReader(final FileChannel channel) {
        this.channel = channel;
    }

    boolean isEOF() {
        return EOF;
    }

    boolean isHasNewLine() {
        return hasNewLine;
    }

    byte[] getData() {
        return data;
    }

    int getSemicolonPos() {
        return semicolonPos;
    }

    int getNewlinePos() {
        return newlinePos;
    }

    int getLineStartPos() {
        return lineStartPos;
    }

    City getCity() {
        return City.of(Arrays.copyOfRange(data, lineStartPos, semicolonPos), hashCode);
    }

    void readNextLine() {
        hashCode = -1;
        hasNewLine = false;

        try {
            if (end - pos < REMAINING_MIN_BUFFERSIZE) {
                System.arraycopy(data, pos, data, 0, data.length - pos);
                end = end - pos;
                pos = 0;
                buffer.position(end);

                if (channel.read(buffer) == -1) {
                    EOF = true;
                }
                end = buffer.position();
            }
        } catch (IOException e) {
            e.printStackTrace();
            EOF = true;
            throw new RuntimeException(e);
        }

        lineStartPos = pos;

        int h = 1;
        int i = pos;
        while (i < end) {
            if (data[i] == ';') {
                semicolonPos = i++;
                break;
            }
            h = (h << 5) - h + data[i];
            i++;
        }
        this.hashCode = h;

        while (i < end) {
            if (data[i] == '\n') {
                newlinePos = i;
                pos = i + 1;
                hasNewLine = true;
                return;
            }
            i++;
        }
        hasNewLine = false;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return new String(data);
    }
}
