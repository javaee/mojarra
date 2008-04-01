				<tr>
					<td>Single-select menu:</td>
					<td><faces:selectone_menu id="HockeyHeroes">
						<faces:selectitem itemValue="10" itemLabel="Guy Lafleur" />
						<faces:selectitem itemValue="99" itemLabel="Wayne Gretzky" />
						<faces:selectitem itemValue="4" itemLabel="Bobby Orr" selected="true" />
						<faces:selectitem itemValue="2" itemLabel="Brad Park" />
						<faces:selectitem itemValue="9" itemLabel="Gordie Howe" />
					</faces:selectone_menu></td>
				</tr>
				<tr>
					<td>Single-select menumodel:</td>
					<td><faces:selectone_menu id="menumodel" size="3">
						<faces:selectitems id="menumodelitems"
							modelReference="${LoginBean.options}" />
					</faces:selectone_menu></td>
				</tr>
