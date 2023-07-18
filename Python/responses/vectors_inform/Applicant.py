class Applicant:
    def __init__(self):
        self.snils = None
        self.bvi = False
        self.additionalPoints = None  # дополнительные баллы
        self.points = None  # баллы за экзамены
        self.namesProfile = None  # названия предметов
        self.originalDocuments = False
        self.consent = False

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