package com.ukreugene.dbclassloader;

import java.io.Writer;

public interface CustomDecompiler {
	void decompile(String className, byte[] classData, Writer writer);
}
