				<tr>
					<td>Multi-select listbox:</td>
					<td><faces:selectmany_listbox id="ManyApples2">
						<faces:selectitem itemValue="0" itemLabel="zero" />
						<faces:selectitem itemValue="1" itemLabel="one" />
						<faces:selectitem itemValue="2" itemLabel="two" />
						<faces:selectitem itemValue="3" itemLabel="three" />
						<faces:selectitem itemValue="4" itemLabel="four" selected="true" />
						<faces:selectitem itemValue="5" itemLabel="five" />
						<faces:selectitem itemValue="6" itemLabel="six" />
						<faces:selectitem itemValue="7" itemLabel="seven" selected="true" />
						<faces:selectitem itemValue="8" itemLabel="eight" />
						<faces:selectitem itemValue="9" itemLabel="nine" />
					</faces:selectmany_listbox></td>
				</tr>
				<tr>
					<td>Multi-select listmodel:</td>
					<td><faces:selectmany_listbox id="listmodel"
						modelReference="LoginBean.currentOption">
						<faces:selectitems id="listmodelitems"
							modelReference="LoginBean.options" />
					</faces:selectmany_listbox></td>
				</tr>
