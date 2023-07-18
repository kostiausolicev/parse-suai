import datetime
import json
import os

import pytz

from responses.vectors_inform.Applicant import Applicant


class ApplicantData:
    def __init__(self):
        self.actuality_date = None
        self.position = None

    def set_actuality_date(self, actuality_date: str):
        self.actuality_date = actuality_date
        return self

    def set_position(self, position: int):
        self.position = position
        return self

    def get_actuality_date(self):
        return self.actuality_date

    def get_position(self):
        return self.position

    def to_dict(self) -> dict:
        d = vars(self)
        return d

    @staticmethod
    def parse_json(vector_name: str, snils: str) -> 'ApplicantData':
        vector_path = "../Python/vectors/" + vector_name + ".json"
        if not os.path.exists(vector_path):
            return ApplicantData()
        with open(vector_path) as file:
            data = json.load(file)
            ApplicantData.update_json(data["update"], vector_path)
        actuality_date = data["update"]
        position = 0
        for count, item in enumerate(data["list"]):
            if item['snils'] == snils:
                position = count + 1
        return ApplicantData().set_actuality_date(actuality_date).set_position(position)

    @staticmethod
    # TODO Сделать парсинг в json когда появятся списки
    def parse_to_json(file_name: str):
        data = {
            "update": datetime.datetime.now(pytz.timezone('Europe/Moscow')).strftime('%Y-%m-%d %H:%M'),
            "list": [
                Applicant().set_snils("111").set_bvi(True).set_additional_points(10).set_points(None).set_names_profile(
                    None).set_original_documents(True).set_consent(True).to_dict(),
                Applicant().set_snils("112").set_bvi(True).set_additional_points(10).set_points(None).set_names_profile(
                    None).set_original_documents(True).set_consent(True).to_dict(),
                Applicant().set_snils("113").set_bvi(False).set_additional_points(10).set_points(
                    [98, 98, 100]).set_names_profile(
                    ["русский язык", "математика профильная", "информатика"]).set_original_documents(
                    False).set_consent(False).to_dict(),
                Applicant().set_snils("114").set_bvi(False).set_additional_points(10).set_points(
                    [98, 94, 98]).set_names_profile(
                    ["русский язык", "математика профильная", "информатика"]).set_original_documents(False).set_consent(False).to_dict(),
                Applicant().set_snils("115").set_bvi(False).set_additional_points(10).set_points(
                    [87, 94, 88]).set_names_profile(
                    ["русский язык", "математика профильная", "информатика"]).set_original_documents(True).set_consent(True).to_dict()
            ]
        }
        with open(file_name, "w") as file:
            file.seek(0)
            json.dump(data, file)

    @staticmethod
    def update_json(last_update_data_str: str, file_name: str):
        tz = pytz.timezone('Europe/Moscow')
        last_update_data = datetime.datetime.strptime(last_update_data_str, '%Y-%m-%d %H:%M').replace(tzinfo=tz)
        current_data = datetime.datetime.now(tz)
        if current_data > last_update_data + datetime.timedelta(1):
            ApplicantData.parse_to_json(file_name)
