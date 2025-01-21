// Preview da Foto:
const imageInput = document.getElementById('imageInput');
const imagePreview = document.getElementById('imagePreview');
const previewPlaceholder = document.getElementById('previewPlaceholder');

imageInput.addEventListener('change', (event) => {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            imagePreview.src = e.target.result;
            imagePreview.classList.remove('hidden');
            previewPlaceholder.classList.add('hidden');
        };
        reader.readAsDataURL(file);
    } else {
        imagePreview.src = '';
        imagePreview.classList.add('hidden');
        previewPlaceholder.classList.remove('hidden');
    }
});

// Preview das Hashtags:

const hashtagInput = document.getElementById('new-tag-input');
const tagsContainer = document.getElementById('tagsContainer');
const hiddenHashtagsInput = document.querySelector('input[name="hashtags"]');
const tags = [];

hashtagInput.addEventListener('keydown', (event) => {
    if (event.key === ',' || event.key === 'Enter') {
        event.preventDefault(); // Evitar que a vírgula fique no campo
        const value = hashtagInput.value.trim().replace(',', '');
        if (value && !tags.includes(value)) {
            tags.push(value);
            addTag(value);
            updateHiddenInput();
        }
        hashtagInput.value = '';
    }
});

function addTag(tag) {
    const tagElement = document.createElement('div');
    tagElement.className = 'flex items-center bg-blue-100 text-blue-700 px-3 py-1 rounded-full text-sm';
    tagElement.textContent = tag;

    const removeButton = document.createElement('button');
    removeButton.className = 'ml-2 text-blue-500 hover:text-blue-700';
    removeButton.textContent = 'x';
    removeButton.onclick = () => {
        tags.splice(tags.indexOf(tag), 1);
        tagsContainer.removeChild(tagElement);
        updateHiddenInput();
    };

    tagElement.appendChild(removeButton);
    tagsContainer.appendChild(tagElement);
}

function updateHiddenInput() {
    hiddenHashtagsInput.value = tags.join(',');
}