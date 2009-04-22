
  
  var lastHighlightedMenu=null;   
  function menuHighlight(menuIndex) {  
    menuDisappear();          
    var myDiv = document.getElementById("menu"+menuIndex); 
    // armazena qual o item de menu foi selecionado
    lastHighlightedMenu=myDiv; 
    myDiv.style.display="block";        
  }

  function menuDisappear() {
    //  retorna true se diferente de null ou undefined
    if(lastHighlightedMenu) 
      lastHighlightedMenu.style.display="none";
  }