class Applicant:
    def __init__(self):
        self.snils = None
        self.bvi = False
        self.priority = None
        self.examsPoints = None
        self.allPoints = None
        self.additionalPoints = None  # дополнительные баллы
        self.points = None  # баллы за экзамены
        self.namesProfile = None  # названия предметов
        self.originalDocuments = False
        self.consent = False

    def get_priority(self):
        return self.priority

    def get_exams_points(self):
        return self.examsPoints

    def get_all_points(self):
        return self.allPoints

    # Сеттеры, возвращающие self
    def set_priority(self, value):
        self.priority = value
        return self

    def set_exams_points(self, value):
        self.examsPoints = value
        return self

    def set_all_points(self, value):
        self.allPoints = value
        return self

    def set_snils(self, snils: str):
        self.snils = snils
        return self

    def set_bvi(self, bvi: bool):
        self.bvi = bvi
        return self

    def set_additional_points(self, additional_points: int):
        self.additionalPoints = additional_points
        return self

    def set_points(self, points: list):
        self.points = points
        return self

    def set_names_profile(self, names_profile: list):
        self.namesProfile = names_profile
        return self

    def set_original_documents(self, original_documents: bool):
        self.originalDocuments = original_documents
        return self

    def set_consent(self, consent: bool):
        self.consent = consent
        return self

    def to_dict(self):
        return vars(self)
