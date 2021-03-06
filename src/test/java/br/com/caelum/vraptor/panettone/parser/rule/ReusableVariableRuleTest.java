package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.TextChunkBuilder;
import br.com.caelum.vraptor.panettone.parser.ast.ReusableVariableNode;

public class ReusableVariableRuleTest {

	private ReusableVariableRule rule;
	
	@Before
	public void setUp() {
		rule = new ReusableVariableRule();
	}
	
	@Test
	public void shouldExtractNameAndCode() {
		SourceCode sc = new SourceCode(
				"@{{body\nbla();ble();\n\nbli();\n@}}\n"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@{{body\nbla();ble();\n\nbli();\n@}}\n", chunks.get(0).getText());
	}

	@Test
	public void shouldAcceptPrintRulesInsideReusableCode() {
		SourceCode sc = new SourceCode(
				"@{{body\n"
				+ "Guilherme @mensagem\n"
				+ "@}}\n"
				+ "	"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@{{body\n"
				+ "Guilherme @mensagem\n"
				+ "@}}\n", chunks.get(0).getText());
	}

	@Test
	public void shouldSupportManyBodies() {
		SourceCode sc = new SourceCode(
				"@{{a\nbla();ble();\n\nbli();\n@}}\n@{{b\nbla();ble();\n\nbli();\n@}}\n"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@{{a\nbla();ble();\n\nbli();\n@}}\n", chunks.get(0).getText());
		Assert.assertEquals("@{{b\nbla();ble();\n\nbli();\n@}}\n", chunks.get(1).getText());
	}
	
	@Test
	public void shouldCreateNode() {
		
		ReusableVariableNode node = (ReusableVariableNode) rule.getNode(TextChunkBuilder.to("@{{body\nbla();\n@}}\n"));
		
		Assert.assertEquals("body", node.getName());
		Assert.assertEquals("bla();", node.getContent());
	}
}
