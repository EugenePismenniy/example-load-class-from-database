/**
 * 
 */
package com.ukreugene.dbclassloader.impl;

import java.io.Writer;

import org.springframework.stereotype.Component;

import com.strobel.assembler.metadata.ArrayTypeLoader;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import com.ukreugene.dbclassloader.CustomDecompiler;

/**
 * @author "evgeniy.pismenny"
 *
 */
@Component
public class ProcyonCustomDecompiler implements CustomDecompiler {

	public void decompile(String className, byte[] classData, Writer writer) {

		DecompilerSettings settings = DecompilerSettings.javaDefaults();
		settings.setTypeLoader(new ArrayTypeLoader(classData));
		PlainTextOutput output = new PlainTextOutput(writer);
		Decompiler.decompile(className, output, settings);
	}
}
