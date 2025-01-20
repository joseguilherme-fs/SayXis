let tags = []; // Lista para armazenar as hashtags
    const tagsList = document.getElementById("tags-list");
    const hashtagsInput = document.getElementById("new-tag-input");

// Atualiza o campo de entrada com as hashtags ao enviar o formulário
    function updateHashtagsInput() {
        const hiddenInput = document.getElementById("hidden-hashtags-input");
        hiddenInput.value = tags.join(",");
    }

    document.querySelector("form").addEventListener("submit", updateHashtagsInput);


});