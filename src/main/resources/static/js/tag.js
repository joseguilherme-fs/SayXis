const hashtagInput = document.querySelector('#edit-tags input[name="comment"]');
const tagsContainer = document.getElementById('new-tags');
const hiddenHashtagsInput = document.querySelector('input[name="hashtags"]');
const tags = [];

hashtagInput.addEventListener('keydown', (event) => {
    if (event.key === ',' || event.key === 'Enter') {
        event.preventDefault();
        const value = hashtagInput.value.trim().replace(',', '');
        if (value && !tags.includes(value)) {
            tags.push(value);
            addTag(value);
            updateHiddenInput();
        }
        hashtagInput.value = '';
    }
});

const existingTags = document.querySelectorAll('#edit-tags .tag');
existingTags.forEach(tagElement => {
    tagElement.addEventListener('click', () => {
        const tagText = tagElement.textContent.replace('#', '').trim();
        if (!tags.includes(tagText)) {
            tags.push(tagText);
            addTag(tagText);
            updateHiddenInput();
        }
    });
});

function addTag(tag) {
    const tagElement = document.createElement('li');
    tagElement.className = 'new-tag bg-blue-100 text-blue-700 px-3 py-1 rounded-full text-sm';
    tagElement.textContent = `#${tag}`;

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
