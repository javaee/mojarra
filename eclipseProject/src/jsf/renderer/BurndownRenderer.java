package jsf.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import jsf.component.BurndownComponent;

import entities.Story;

public class BurndownRenderer extends Renderer {

	@Override
	public void encodeBegin(FacesContext $context, UIComponent $component)
			throws IOException {
		System.out.println("RENDERER 1");
		super.encodeBegin($context, $component);
		System.out.println("RENDERER 2");
		BurndownComponent burndownComponent = (BurndownComponent) $component;
		ResponseWriter writer = $context.getResponseWriter();
		this.encodeChart(writer, burndownComponent);
		this.encodeList(writer, burndownComponent);
		writer.close();
		System.out.println("RENDERER 3");
		
	}

	private void encodeChart(ResponseWriter $writer,
			BurndownComponent $burndownComponent) throws IOException {
		// TODO Auto-generated method stub
		
	}

	private void encodeList(ResponseWriter $writer,
			BurndownComponent $burndownComponent) throws IOException {
		$writer.write("<table>");
		for (Story story: $burndownComponent.getSprint().getStories()){
			$writer.write("<tr>");
			$writer.write(String.format("<td>%s</td>", story.getName()));
			$writer.write(String.format("<td>%s</td>", story.getPriority()));
			$writer.write(String.format("<td>%s</td>", story.getEstimation()));
			$writer.write("</tr>");
		}//for
		$writer.write("</table>");
		
	}

}
