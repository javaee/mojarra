<!--
 Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

           <tr>

             <td>

               <h:output_text id="panel1_label"
                     value="list with stylesheets."/>

             </td>
                   

             <td>
                 <h:panel_list id="list1"
                 columnClasses="list-column-center,list-column-left,
                 list-column-center, list-column-right"
                 headerClass="list-header"
                  panelClass="list-background"
                  rowClasses="list-row-even,list-row-odd">

                  <!-- Column Headings -->

                  <f:facet name="header">
		    <h:panel_group>
		      <h:output_text id="accountIdHeading1"
				  value="Account Id"/>
		      <h:output_text id="nameHeading1"
				  value="Customer Name"/>
		      <h:output_text id="symbolHeading1"
				  value="Symbol"/>
		      <h:output_text id="totalSalesHeading1"
				  value="Total Sales"/>
		    </h:panel_group>
                  </f:facet>

                  <!-- List Data -->

                  <h:panel_data    id="listData1" var="customer"
                       valueRef="ListBean">
                    <h:output_text id="accountId1"
                       valueRef="customer.accountId"/>
                    <h:output_text id="name1"
                       valueRef="customer.name"/>
                    <h:output_text id="symbol1"
                       valueRef="customer.symbol"/>
                    <h:output_text id="totalSales1"
                       valueRef="customer.totalSales"/>
                  </h:panel_data>

                 </h:panel_list> 
             </td>

            </tr>
          
            <tr>

             <td>

               <h:output_text id="panel2_label" 
                     value="list with HTML 4.0 attribute, headers, footers, and no stylesheets."/>

             </td>
                   
            
             <td>
                 <h:panel_list id="list2"
                  border="1" cellpadding="3" cellspacing="3"
                     summary="List with HTML attributes."
                     title="List with no stylesheets" >

                  <!-- Headers -->

                  <f:facet name="header">
		    <h:panel_group>
		      <h:output_text id="accountIdHeading2"
				  value="Account Id"/>
		      <h:output_text id="nameHeading2"
				  value="Customer Name"/>
		      <h:output_text id="symbolHeading2"
				  value="Symbol"/>
		      <h:output_text id="totalSalesHeading2"
				  value="Total Sales"/>
		    </h:panel_group>
                  </f:facet>

                  <!-- List Data -->

                  <h:panel_data    id="listData2" var="customer"
                       valueRef="ListBean">
                    <h:output_text id="accountId2"
                       valueRef="customer.accountId"/>
                    <h:output_text id="name2"
                       valueRef="customer.name"/>
                    <h:output_text id="symbol2"
                       valueRef="customer.symbol"/>
                    <h:output_text id="totalSales2"
                       valueRef="customer.totalSales"/>
                  </h:panel_data>

                  <!-- Footers -->

                  <f:facet name="footer">
		    <h:panel_group>
		      <h:output_text id="accountIdFooting1"
				  value="Account Id"/>
		      <h:output_text id="nameFooting1"
				  value="Customer Name"/>
		      <h:output_text id="symbolFooting1"
				  value="Symbol"/>
		      <h:output_text id="totalSalesFooting1"
				  value="Total Sales"/>
		    </h:panel_group>
                  </f:facet>



                 </h:panel_list> 
             </td>

            </tr>




