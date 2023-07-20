import datetime
import json
import os

import bs4
import pytz
import requests

from responses.applicants.ApplicantData import ApplicantData
from responses.vectors_inform.Applicant import Applicant


class Utils:
    @staticmethod
    def parse_json(vector_name: str, snils: str, t: str) -> 'ApplicantData':
        vector_path = "../Python/vectors/" + vector_name + "&" + t + ".json"
        if not os.path.exists(vector_path):
            return ApplicantData()
        with open(vector_path, encoding="utf-8") as file:
            with open("../Python/vectors/all_vectors_information.json", encoding="utf-8") as file1:
                d = json.load(file1)
                match t:
                    case "budget": url = d[vector_name]["link_budget"]
                    case "contract": url = d[vector_name]["link_contract"]
                    case "special": url = d[vector_name]["link_special"]
                    case "separate": url = d[vector_name]["link_separate"]
                    case "contract_abroad": url = d[vector_name]["link_contract_abroad"]
                    case _: return ApplicantData()
            data = json.load(file)
            Utils.update_json(data["update"], vector_path, url)
        actuality_date = data["update"]
        position = 0
        for count, item in enumerate(data["list"]):
            if item['snils'] == snils:
                position = count + 1
        return ApplicantData().set_actuality_date(actuality_date).set_position(position)

    @staticmethod
    def parse_to_json(file_name: str, url: str):
        def conv_to_ap_obj(h: list, s: list):
            r = []
            for a in s:
                applicant = Applicant()
                for head in h:
                    match head:
                        case "СНИЛС/Идентификатор":
                            applicant.set_snils(a[1])
                        case "Приоритет":
                            applicant.set_priority(int(a[2]))
                        case "Сумма конкурсных баллов":
                            applicant.set_all_points(int(a[3]))
                        case "Сумма баллов ВИ":
                            applicant.set_exams_points(int(a[4]))
                        case "Баллы за достижения":
                            applicant.set_additional_points(int(a[5]))
                        case _:
                            continue
                r.append(applicant.to_dict())
            return r

        response = requests.get(url)
        html = response.text
        soup = bs4.BeautifulSoup(html, 'html.parser')
        table_div = soup.find('div', class_='table-responsive')
        table = table_div.find('table')
        headers = []
        for head in table.find('thead').find_all('tr'):
            headers = [data.text.strip() for data in head.find_all('th')]
        rows = []
        for row in table.find('tbody').find_all('tr'):
            row_data = [data.text.strip() if data.text.strip() != '' else '0' for data in row.find_all('td')]
            rows.append(row_data)
        rows.sort(key=lambda x: (-int(x[3]), -int(x[4])))
        list_ = conv_to_ap_obj(headers, rows)
        data = {
            "update": datetime.datetime.now(pytz.timezone('Europe/Moscow')).strftime('%Y-%m-%d %H:%M'),
            "headers": headers,
            "list": list_
        }
        with open(file_name, "w", encoding="utf-8") as file:
            file.seek(0)
            json.dump(data, file, ensure_ascii=False)

    @staticmethod
    def update_json(last_update_data_str: str, file_name: str, url: str):
        tz = pytz.timezone('Europe/Moscow')
        last_update_data = datetime.datetime.strptime(last_update_data_str, '%Y-%m-%d %H:%M').replace(tzinfo=tz)
        current_data = datetime.datetime.now(tz)
        if current_data > last_update_data + datetime.timedelta(1):
            Utils.parse_to_json(file_name, url)
