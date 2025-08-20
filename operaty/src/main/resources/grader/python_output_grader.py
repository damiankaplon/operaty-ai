from typing import Any


def calculate_json_similarity(json1: dict, json2: dict) -> float:
    total_fields = len(set(json1.keys()) | set(json2.keys()))
    if total_fields == 0:
        return 1.0

    matching_fields = 0
    for key in set(json1.keys()) | set(json2.keys()):
        if key in json1 and key in json2:
            if isinstance(json1[key], dict) and isinstance(json2[key], dict):
                matching_fields += calculate_json_similarity(json1[key], json2[key])
            elif json1[key] == json2[key]:
                matching_fields += 1

    return matching_fields / total_fields


def grade(sample: dict[str, Any], item: dict[str, Any]) -> float:
    road_report = sample['output_json']
    expected_output = item['expected_output']

    if road_report == expected_output:
        return 1.0

    return calculate_json_similarity(road_report, expected_output)
