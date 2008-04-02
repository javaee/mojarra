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

                  <h:panel_group   id="listHeadings1">
                    <h:output_text id="accountIdHeading1"
                                value="Account Id"/>
                    <h:output_text id="nameHeading1"
                                value="Customer Name"/>
                    <h:output_text id="symbolHeading1"
                                value="Symbol"/>
                    <h:output_text id="totalSalesHeading1"
                                value="Total Sales"/>
                  </h:panel_group>

                  <!-- List Data -->

                  <h:panel_data    id="listData1" var="customer"
                       modelReference="ListBean">
                    <h:output_text id="accountId1"
                       modelReference="customer.accountId"/>
                    <h:output_text id="name1"
                       modelReference="customer.name"/>
                    <h:output_text id="symbol1"
                       modelReference="customer.symbol"/>
                    <h:output_text id="totalSales1"
                       modelReference="customer.totalSales"/>
                  </h:panel_data>

                 </h:panel_list> 
             </td>

            </tr>
          
            <tr>

             <td>

               <h:output_text id="panel2_label" 
                     value="list with HTML 4.0 attributes and no stylesheets."/>

             </td>
                   
            
             <td>
                 <h:panel_list id="list2"
                  border="1" cellpadding="3" cellspacing="3"
                     summary="List with HTML attributes."
                     title="List with no stylesheets" >

                  <!-- List Data -->

                  <h:panel_data    id="listData2" var="customer"
                       modelReference="ListBean">
                    <h:output_text id="accountId2"
                       modelReference="customer.accountId"/>
                    <h:output_text id="name2"
                       modelReference="customer.name"/>
                    <h:output_text id="symbol2"
                       modelReference="customer.symbol"/>
                    <h:output_text id="totalSales2"
                       modelReference="customer.totalSales"/>
                  </h:panel_data>

                 </h:panel_list> 
             </td>

            </tr>




