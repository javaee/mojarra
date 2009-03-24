package jsf.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import jsf.component.BurndownComponent;

import entities.Story;

public class BurndownRenderer extends Renderer {

	public BurndownRenderer(){
		System.out.printf("%s %n%n",this.getClass().getSimpleName());
	}
	
	@Override
	public void encodeBegin(FacesContext $context, UIComponent $component)
			throws IOException {
		super.encodeBegin($context, $component);
		BurndownComponent burndownComponent = (BurndownComponent) $component;
		ResponseWriter writer = $context.getResponseWriter();
		if (burndownComponent != null && burndownComponent.getSprint() != null){
			this.encodeChart(writer, burndownComponent);
			this.encodeList(writer, burndownComponent);
		} else {
			this.encodeEmpty(writer);
		}//if
		writer.close();
		
	}

	private void encodeEmpty(ResponseWriter $writer) throws IOException {
		$writer.write("<div>");
		$writer.write("<label>No sprint assigned </label>");
		$writer.write("</div>");
	}

	private void encodeChart(ResponseWriter $writer,
			BurndownComponent $burndownComponent) throws IOException {
		$writer.write("<div>");
		$writer.write("<img src=\"http://chart.apis.google.com/chart?chs=500x225&chd=t:0,10,20,30,40,50,60,70,80,90,100|100,90,80,70,60,50,40,30,20,10,0|0,10,20,30,40,50,60,70,80,90,100,110|97.5,97.5,77.5,77.5,57.5,57.5,57.5,52.5,52.5,52.5,52.5&cht=lxy&chxt=x,y&chxr=0,0,11|1,0,40&chdl=Previsto|Real&chco=333366,0000ff&chls=3,6,3|1,1,0&chg=10,10,1,5&chtt=3,9sp/dia\" />");
		$writer.write("</div>");
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
