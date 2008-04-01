           <tr>

             <td>

               <faces:output_text id="panel1_label" 
                     value="list with stylesheets."/>

             </td>
                   

             <td>
                 <faces:panel_list id="list1"
                 columnClasses="list-column-center,list-column-left,
                 list-column-center, list-column-right"
                 headerClass="list-header"
                  panelClass="list-background"
                  rowClasses="list-row-even,list-row-odd">

                  <!-- Column Headings -->

                  <faces:panel_group   id="listHeadings1">
                    <faces:output_text id="accountIdHeading1"
                                value="Account Id"/>
                    <faces:output_text id="nameHeading1"
                                value="Customer Name"/>
                    <faces:output_text id="symbolHeading1"
                                value="Symbol"/>
                    <faces:output_text id="totalSalesHeading1"
                                value="Total Sales"/>
                  </faces:panel_group>

                  <!-- List Data -->

                  <faces:panel_data    id="listData1" var="customer"
                       modelReference="ListBean">
                    <faces:output_text id="accountId1"
                       modelReference="customer.accountId"/>
                    <faces:output_text id="name1"
                       modelReference="customer.name"/>
                    <faces:output_text id="symbol1"
                       modelReference="customer.symbol"/>
                    <faces:output_text id="totalSales1"
                       modelReference="customer.totalSales"/>
                  </faces:panel_data>

                 </faces:panel_list> 
             </td>

            </tr>
          
            <tr>

             <td>

               <faces:output_text id="panel2_label" 
                     value="list with HTML 4.0 attributes and no stylesheets."/>

             </td>
                   
            
             <td>
                 <faces:panel_list id="list2"
                  border="1" cellpadding="3" cellspacing="3"
                     summary="List with HTML attributes."
                     title="List with no stylesheets" >

                  <!-- List Data -->

                  <faces:panel_data    id="listData2" var="customer"
                       modelReference="ListBean">
                    <faces:output_text id="accountId2"
                       modelReference="customer.accountId"/>
                    <faces:output_text id="name2"
                       modelReference="customer.name"/>
                    <faces:output_text id="symbol2"
                       modelReference="customer.symbol"/>
                    <faces:output_text id="totalSales2"
                       modelReference="customer.totalSales"/>
                  </faces:panel_data>

                 </faces:panel_list> 
             </td>

            </tr>




