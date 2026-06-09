import os
import re

SCREENS_DIR = r"d:\Semester 4\TTD\app\src\main\java\com\example\tamtamduku\ui\screens"
COMPONENTS_DIR = r"d:\Semester 4\TTD\app\src\main\java\com\example\tamtamduku\ui\components"

# Dictionaries for new strings
strings_id = {}
strings_en = {}

COLOR_MAPPING = {
    "Color.Black": "MaterialTheme.colorScheme.onBackground",
    "Color.White": "MaterialTheme.colorScheme.background",
    "Color(0xFFFFFDF8)": "MaterialTheme.colorScheme.background",
    "Color(0xFFFFFDFB)": "MaterialTheme.colorScheme.background",
    "Color(0xFFFF7A00)": "MaterialTheme.colorScheme.primary",
    "Color(0xFFF97316)": "MaterialTheme.colorScheme.primary",
    "Color.Gray": "MaterialTheme.colorScheme.onSurfaceVariant",
    "Color.LightGray": "MaterialTheme.colorScheme.outlineVariant",
    "Color.DarkGray": "MaterialTheme.colorScheme.secondaryContainer",
    "Color(0xFFE0E0E0)": "MaterialTheme.colorScheme.outline",
    "Color(0xFFFDECDA)": "MaterialTheme.colorScheme.primaryContainer",
    "Color(0xFFFFF0E5)": "MaterialTheme.colorScheme.primaryContainer",
    "Color(0xFFE0DDD8)": "MaterialTheme.colorScheme.outlineVariant",
    "Color(0xFFF5F0EA)": "MaterialTheme.colorScheme.outlineVariant",
    "Color(0xFFF9F6F0)": "MaterialTheme.colorScheme.surfaceVariant",
    "Color.Red": "MaterialTheme.colorScheme.error",
    "Color(0xFFF44336)": "MaterialTheme.colorScheme.error",
    "Color(0xFF4CAF50)": "Color(0xFF4CAF50)", # success color maybe leave it or map to tertiary
}

def to_snake_case(text):
    text = re.sub(r'[^a-zA-Z0-9\s]', '', text)
    text = text.strip().lower()
    text = re.sub(r'\s+', '_', text)
    if not text:
        return "str_empty"
    # limit length
    words = text.split('_')
    return '_'.join(words[:5])

def translate_to_en(text):
    # This is a dummy translator, in reality we'll just put the indonesian string with a prefix
    # and manually fix the xml later, or let the user do it, wait, user said "keseluruhan diubah ke bahasa inggris".
    # I should try to provide an english translation if I can?
    # I can't easily translate in python without an API.
    # So I will just write a python script that replaces the color first, and then prints out all strings so I can review them.
    pass

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original_content = content
    # Replace colors
    for old_color, new_color in COLOR_MAPPING.items():
        if new_color != old_color:
            content = content.replace(old_color, new_color)

    # Find all strings in Text("...") or Text(text = "...")
    # This regex is a bit simplistic but works for many cases
    string_pattern = r'(?<!R\.string\.)(?<!modifier = Modifier\.)(?<!Icon\(imageVector = Icons\.)(?<!Icon\()(?<!Modifier\.)(?:text\s*=\s*|Text\(\s*|label\s*=\s*\{?\s*Text\(\s*)?\"([^\"]+)\"'
    
    matches = re.finditer(string_pattern, content)
    
    # We will manually replace strings using the agent to provide good translations.
    # The script will just replace colors for now, to save me a lot of time.
    
    if content != original_content:
        # add MaterialTheme import if needed
        if "MaterialTheme" in content and "androidx.compose.material3.MaterialTheme" not in content:
            content = "import androidx.compose.material3.MaterialTheme\n" + content
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated colors in {filepath}")

for root, _, files in os.walk(SCREENS_DIR):
    for f in files:
        if f.endswith('.kt'):
            process_file(os.path.join(root, f))

for root, _, files in os.walk(COMPONENTS_DIR):
    for f in files:
        if f.endswith('.kt'):
            process_file(os.path.join(root, f))
