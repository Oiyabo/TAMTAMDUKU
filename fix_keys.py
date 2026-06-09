import os

replacements = {
    '0812xxxxxxxx': 'phone_0812xxxxxxxx',
    '50_328': 'rating_50_328',
    '23_april_2026': 'date_23_april_2026',
    '32_km': 'distance_32_km',
    '120_ulasan': 'review_120_ulasan'
}

# 1. Update strings.xml
def update_xml(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    for old, new in replacements.items():
        content = content.replace(f'name="{old}"', f'name="{new}"')
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

update_xml(r'd:\Semester 4\TTD\app\src\main\res\values\strings.xml')
update_xml(r'd:\Semester 4\TTD\app\src\main\res\values-en\strings.xml')

# 2. Update .kt files
SCREENS_DIR = r'd:\Semester 4\TTD\app\src\main\java\com\example\tamtamduku\ui\screens'
COMPONENTS_DIR = r'd:\Semester 4\TTD\app\src\main\java\com\example\tamtamduku\ui\components'

def update_kt(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    orig = content
    for old, new in replacements.items():
        content = content.replace(f'R.string.{old}', f'R.string.{new}')
    if content != orig:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Fixed {filepath}")

for root, _, files in os.walk(SCREENS_DIR):
    for f in files:
        if f.endswith('.kt'):
            update_kt(os.path.join(root, f))

for root, _, files in os.walk(COMPONENTS_DIR):
    for f in files:
        if f.endswith('.kt'):
            update_kt(os.path.join(root, f))

print('Fixed invalid keys.')
