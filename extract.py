import os
import re
import json

SCREENS_DIR = r"d:\Semester 4\TTD\app\src\main\java\com\example\tamtamduku\ui\screens"
COMPONENTS_DIR = r"d:\Semester 4\TTD\app\src\main\java\com\example\tamtamduku\ui\components"

string_map = {}

def to_snake_case(text):
    text = re.sub(r'[^a-zA-Z0-9\s]', '', text)
    text = text.strip().lower()
    text = re.sub(r'\s+', '_', text)
    if not text:
        return "str_empty"
    words = text.split('_')
    return '_'.join(words[:5])

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original_content = content
    
    # We want to match: Text("some string") or text = "some string" or placeholder = { Text("some string") }
    # Let's match any literal string "..." that is the argument to text or Text()
    # To be safe, we only match: Text(text = "...") or Text("...") or label = "..." or title = "..." or subtitle = "..."
    
    # Regex 1: Text("...")
    pattern1 = r'Text\(\s*\"([^\"]+)\"'
    def repl1(m):
        val = m.group(1)
        if '$' in val: return m.group(0) # skip templates
        key = to_snake_case(val)
        string_map[key] = val
        return f'Text(stringResource(R.string.{key})'
    
    content = re.sub(pattern1, repl1, content)
    
    # Regex 2: text = "..."
    pattern2 = r'text\s*=\s*\"([^\"]+)\"'
    def repl2(m):
        val = m.group(1)
        if '$' in val: return m.group(0) # skip templates
        key = to_snake_case(val)
        string_map[key] = val
        return f'text = stringResource(R.string.{key})'
    
    content = re.sub(pattern2, repl2, content)
    
    # Regex 3: label = "..."
    pattern3 = r'label\s*=\s*\"([^\"]+)\"'
    def repl3(m):
        val = m.group(1)
        if '$' in val: return m.group(0) # skip templates
        key = to_snake_case(val)
        string_map[key] = val
        return f'label = stringResource(R.string.{key})'
    
    content = re.sub(pattern3, repl3, content)

    # Regex 4: title = "..."
    pattern4 = r'title\s*=\s*\"([^\"]+)\"'
    def repl4(m):
        val = m.group(1)
        if '$' in val: return m.group(0) # skip templates
        key = to_snake_case(val)
        string_map[key] = val
        return f'title = stringResource(R.string.{key})'
    
    content = re.sub(pattern4, repl4, content)

    # Regex 5: subtitle = "..."
    pattern5 = r'subtitle\s*=\s*\"([^\"]+)\"'
    def repl5(m):
        val = m.group(1)
        if '$' in val: return m.group(0) # skip templates
        key = to_snake_case(val)
        string_map[key] = val
        return f'subtitle = stringResource(R.string.{key})'
    
    content = re.sub(pattern5, repl5, content)
    
    if content != original_content:
        imports = []
        if "androidx.compose.ui.res.stringResource" not in content:
            imports.append("import androidx.compose.ui.res.stringResource")
        if "com.example.tamtamduku.R" not in content:
            imports.append("import com.example.tamtamduku.R")
        
        if imports:
            lines = content.split('\n')
            for i, line in enumerate(lines):
                if line.startswith("import "):
                    lines.insert(i, '\n'.join(imports))
                    break
            content = '\n'.join(lines)
            
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)

for root, _, files in os.walk(SCREENS_DIR):
    for f in files:
        if f.endswith('.kt'):
            process_file(os.path.join(root, f))

for root, _, files in os.walk(COMPONENTS_DIR):
    for f in files:
        if f.endswith('.kt'):
            process_file(os.path.join(root, f))

with open(r'd:\Semester 4\TTD\extracted_strings.json', 'w', encoding='utf-8') as f:
    json.dump(string_map, f, indent=4)
