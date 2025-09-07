import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
  // detect user language
  // learn more: https://github.com/i18next/i18next-browser-languageDetector
  .use(LanguageDetector)
  // pass the i18n instance to react-i18next.
  .use(initReactI18next)
  // init i18next
  // for all options read: https://www.i18next.com/overview/configuration-options
  .init({
    debug: true,
    fallbackLng: 'en',
    interpolation: {
      escapeValue: false, // not needed for react as it escapes by default
    },
    resources: {
      fr: {
        translation: {
          'header.title': 'Gestionnaire de configuration',
          'header.title.apps': 'Gestion des valeurs',
          'header.title.config-helper': 'Gestion des clés',
          'header.title.global-variables': 'Variables globales',
          'header.archives': 'Archives',

          'archive': 'Archiver',
          'unarchive': 'Désarchiver',
          
          'profile.application.title': 'Applications',
          'profile.adminuser.filter.minimumadvise': '3 caractères minimum',
          'profile.adminuser.filter.placeholder': 'Rechercher un utilisateur...',
          'profile.adminuser.filter.title': 'Utilisateurs',
          'profile.admindemand.title': 'Demande(s) en attente',
          'profile.admindemand.empty': 'Aucune demande',
          'profile.application.search.placeholder': 'Rechercher une application...',
          'profile.global.username': 'Nom de l\'utilisateur',
          'profile.global.email': 'Adresse e-mail',
          'profile.global.firstName': 'Prénom',
          'profile.global.lastName': 'Nome de famille',
          
          'applist.application.unknowlastdeliverydate': 'Date de dernière livraison inconnue',
          'applist.application.name': 'Nom de l\'application',
          'applist.application.productowner': 'Owner de l\'application',
          'applist.search.placeholder': 'Rechercher une application',
          'applist.noapplication': 'Aucune application trouvées',
          'applist.title': 'Applications',
          'applist.add': 'Ajouter une application',
          'applist.add.prompt': 'Nom de la nouvelle application ?',

          'appdetails.noconfiguration': 'Aucune configuration trouvée',
          'appdetails.search.placeholder': 'Rechercher une propriété',
          'appdetails.novalue': 'Pas de valeur',
          'appdetails.table.title.file': 'Nom de fichier',
          'appdetails.table.title.key': 'Clé',
          'appdetails.createsnapshot': 'Créer la version snapshot',
          
          'confighelper.existingkey.title': 'Clés existantes',
          'confighelper.params.title.application': 'Applications',
          'confighelper.params.title.versions': 'Versions',
          'confighelper.params.title.files': 'Fichiers',
          'confighelper.params.title.environment': 'Environnements',
          'confighelper.params.title.testing': 'Tester',
          'confighelper.params.title.newfilename': 'Nouveau fichier',
          'confighelper.params.title.replacefile': 'Remplacer le fichier existant',
          'confighelper.params.title.save': 'Sauvegarder',
          'confighelper.params.title.newkey': 'Nouvelle clé',
          'confighelper.params.title.defaultvalue': 'Valuer par défaut',
          'confighelper.params.title.selectedfilename': 'Ajouter au fichier actuel --> ',
          'confighelper.title.testcontent': 'Contenu du fichier de test',
          'confighelper.title.testresult': 'Résultat après traitement',
          'confighelper.addversion': 'Ajouter une version',
          'confighelper.addversion.prompt': 'Nom de la nouvelle version ?',
          'confighelper.addproperty': 'Ajouter une propriété',
          'confighelper.addproperty.filename': 'Nom du fichier ?',
          'confighelper.addproperty.key': 'Clé de la propriété ?',

          'profil.title': 'Profil',
          'profil.roles': 'Rôles',
          
          'globalvariable.addnew': 'Ajouter une nouvelle variable globale',
          'globalvariable.addnew.btn': 'Ajouter',
          'globalvariable.editvalue': 'Valeur de remplacement',
          'globalvariable.remove.btn': 'Supprimer',
          'globalvariable.remove.confirm': 'Confirmer la suppression ?',
          'globalvariable.newkey': 'Nouvelle clé',
          
        }
      },
      en: {
        translation: {
	      'header.title': 'Configuration Manager',
          'header.title.apps': 'Value management',
          'header.title.config-helper': 'Key management',
          'header.title.global-variables': 'Global Variables',
          'header.archives': 'Archives',

          'archive': 'Archive',
          'unarchive': 'Unarchive',
          
          'profile.application.title': 'Applications',
          'profile.adminuser.filter.minimumadvise': 'minimum 3 characters',
          'profile.adminuser.filter.placeholder': 'Finding a user...',
          'profile.adminuser.filter.title': 'Users',
          'profile.admindemand.title': 'Pending request(s)',
          'profile.admindemand.empty': 'No request',
          'profile.application.search.placeholder': 'Search for an application...',
          'profile.global.username': 'Username',
          'profile.global.email': 'Email address',
          'profile.global.firstName': 'FirstName',
          'profile.global.lastName': 'Last Name',
          
          'applist.application.unknowlastdeliverydate': 'Unknown last delivery date',
          'applist.application.name': 'Application name',
          'applist.application.productowner': 'Application Owner',
          'applist.search.placeholder': 'Find an application',
          'applist.noapplication': 'No applications found',
          'applist.title': 'Apps',
          'applist.add': 'Add application',
          'applist.add.prompt': 'New application name?',

          'appdetails.noconfiguration': 'No configuration found',
          'appdetails.search.placeholder': 'Find a property',
          'appdetails.novalue': 'No value',
          'appdetails.table.title.file': 'File name',
          'appdetails.table.title.key': 'Key',
          'appdetails.createsnapshot': 'Create snapshot version',
          
          'confighelper.existingkey.title': 'Existing keys',
          'confighelper.params.title.application': 'Applications',
          'confighelper.params.title.versions': 'Versions',
          'confighelper.params.title.files': 'Files',
          'confighelper.params.title.environment': 'Environments',
          'confighelper.params.title.testing': 'Testing',
          'confighelper.params.title.newfilename': 'New file',
          'confighelper.params.title.replacefile': 'Replace existing file',
          'confighelper.params.title.save': 'Save',
          'confighelper.params.title.newkey': 'New key',
          'confighelper.params.title.defaultvalue': 'Value by default',
          'confighelper.params.title.selectedfilename': 'Add to current file --> ',
          'confighelper.title.testcontent': 'Test file content',
          'confighelper.title.testresult': 'Result after processing',
          'confighelper.addversion': 'Add version',
          'confighelper.addversion.prompt': 'New version name?',
          'confighelper.addproperty': 'Add property',
          'confighelper.addproperty.filename': 'File name?',
          'confighelper.addproperty.key': 'Property key?',

          'profil.title': 'Profile',
          'profil.roles': 'Roles',
          
          'globalvariable.addnew': 'Add new global variable',
          'globalvariable.addnew.btn': 'Add',
          'globalvariable.editvalue': 'Replace value',
          'globalvariable.remove.btn': 'Remove',
          'globalvariable.remove.confirm': 'Confirm removal?',
          'globalvariable.newkey': 'New key',
          
        }
      },
      de: {
        translation: {
		  'header.title': 'Konfigurationsmanager',
          'header.title.apps': 'Wertverwaltung',
          'header.title.config-helper': 'Schlüsselverwaltung',
          'header.title.global-variables': 'Globale Variablen',
          'header.archives': 'Archive',

          'archive': 'Archivieren',
          'unarchive': 'Wiederherstellen',
          
          'profile.application.title': 'Bewerbungen',
          'profile.adminuser.filter.minimumadvise': 'mindestens 3 Zeichen',
          'profile.adminuser.filter.placeholder': 'Benutzer finden...',
          'profile.adminuser.filter.title': 'Benutzer',
          'profile.admindemand.title': 'Ausstehende Anfrage(n)',
          'profile.admindemand.empty': 'Keine Anfrage',
          'profile.application.search.placeholder': 'Nach einer Anwendung suchen...',
          'profile.global.username': 'Benutzername',
          'profile.global.email': 'E-Mail-Adresse',
          'profile.global.firstName': 'Vorname',
          'profile.global.lastName': 'Nachname',
          
          'applist.application.unknowlastdeliverydate': 'Unbekanntes letztes Lieferdatum',
          'applist.application.name': 'Anwendungsname',
          'applist.application.productowner': 'Anwendungseigentümer',
          'applist.search.placeholder': 'Anwendung finden',
          'applist.noapplication': 'Keine Anwendungen gefunden',
          'applist.title': 'Apps',
          'applist.add': 'Anwendung hinzufügen',
          'applist.add.prompt': 'Name der neuen Anwendung?',

          'appdetails.noconfiguration': 'Keine Konfiguration gefunden',
          'appdetails.search.placeholder': 'Konfiguration finden',
          'appdetails.novalue': 'Kein Wert',
          'appdetails.table.title.file': 'Dateiname',
          'appdetails.table.title.key': 'Schlüssel',
          'appdetails.createsnapshot': 'Snapshot-Version erstellen',
          
          'confighelper.existingkey.title': 'Vorhandene Schlüssel',
          'confighelper.params.title.application': 'Anwendungen',
          'confighelper.params.title.versions': 'Versionen',
          'confighelper.params.title.files': 'Dateien',
          'confighelper.params.title.environment': 'Umgebungen',
          'confighelper.params.title.testing': 'Testen',
          'confighelper.params.title.newfilename': 'Neue Datei',
          'confighelper.params.title.replacefile': 'Vorhandene Datei ersetzen',
          'confighelper.params.title.save': 'Speichern',
          'confighelper.params.title.newkey': 'Neuer Schlüssel',
          'confighelper.params.title.defaultvalue': 'Standardwert',
          'confighelper.params.title.s selectedfilename': 'Zur aktuellen Datei hinzufügen --> ',
          'confighelper.title.testcontent': 'Inhalt der Testdatei',
          'confighelper.title.testresult': 'Ergebnis nach Verarbeitung',
          'confighelper.addversion': 'Version hinzufügen',
          'confighelper.addversion.prompt': 'Name der neuen Version?',
          'confighelper.addproperty': 'Eigenschaft hinzufügen',
          'confighelper.addproperty.filename': 'Dateiname?',
          'confighelper.addproperty.key': 'Schlüssel der Eigenschaft?',

          'profil.title': 'Profil',
          'profil.roles': 'Rollen',
          
          'globalvariable.addnew': 'Neue globale Variable hinzufügen',
          'globalvariable.addnew.btn': 'Hinzufügen',
          'globalvariable.editvalue': 'Wert ersetzen',
          'globalvariable.remove.btn': 'Entfernen',
          'globalvariable.remove.confirm': 'Entfernung bestätigen?',
          'globalvariable.newkey': 'Neuer Schlüssel',
          
        }
      },
      lu: {
        translation: {
	
          'header.title': 'Konfiguratiounsmanager',
          'header.title.apps': 'Wäertemanagement',
          'header.title.config-helper': 'Schlësselmanagement',
          'header.title.global-variables': 'Global Variablen',
          'header.archives': 'Archive',
          
          'profile.application.title': 'Uwendungen',
          'profile.adminuser.filter.minimumadvise': 'minimum 3 Zeechen',
          'profile.adminuser.filter.placeholder': 'E Benotzer fannen...',
          'profile.adminuser.filter.title': 'Benotzer',
          'profile.admindemand.title': 'Ufro(en) pendend',
          'profile.admindemand.empty': 'Keng Ufro',
          'profile.application.search.placeholder': 'Sich no enger Applikatioun...',
          'profile.global.username': 'Benotzernumm',
          'profile.global.email': 'E-Mailadress',
          'profile.global.firstName': 'Virnumm',
          'profile.global.lastName': 'Numm',
          
          'applist.application.unknowlastdeliverydate': 'Onbekannt lescht Liwwerungsdatum',
          'applist.application.name': 'Uwendungsnumm',
          'applist.application.producttowner': 'Proprietaire vun der Applikatioun',
          'applist.search.placeholder': 'Eng Applikatioun fannen',
          'applist.noapplication': 'Keng Uwendunge fonnt',
          'applist.title': 'Apps',

          'appdetails.noconfiguration': 'Keng Konfiguratioun fonnt',
          'appdetails.search.placeholder': 'Eng Konfiguratioun fannen',
          'appdetails.novalue': 'Kee Wäert',
          'appdetails.table.title.file': 'Numm vum Fichier',
          'appdetails.table.title.key': 'Schlëssel',
          'appdetails.createsnapshot': 'Snapshot Versioun erstellen',
          
          'confighelper.existingkey.title': 'Existéierend Schlësselen',
          'confighelper.params.title.application': 'Applikatiounen',
          'confighelper.params.title.versions': 'Versiounen',
          'confighelper.params.title.files': 'Dateien',
          'confighelper.params.title.environment': 'Ëmfeld',
          'confighelper.params.title.testing': 'Test',
          'confighelper.params.title.newfilename': 'Neie Fichier',
          'confighelper.params.title.replacefile': 'bestehend Datei ersetzen',
          'confighelper.params.title.save': 'Späicheren',
          'confighelper.params.title.newkey': 'Neie Schlëssel',
          'confighelper.params.title.defaultvalue': 'Default Wäert',
          'confighelper.params.title.selectedfilename': 'An den aktuelle Fichier bäifügen --> ',
          'confighelper.title.testcontent': 'Test Datei Inhalt',
          'confighelper.title.testresult': 'Resultat no der Veraarbechtung',

          'profil.title': 'Profil',
          'profil.roles': 'Rollen',
          
          'globalvariable.addnew': 'Nei global Variabel derbäisetzen',
          'globalvariable.addnew.btn': 'Addéieren',
          'globalvariable.editvalue': 'Wäert ersetzen',
          'globalvariable.remove.btn': 'Ewechmaachen',
          'globalvariable.remove.confirm': 'Entfernung bestätegt?',
          'globalvariable.newkey': 'Neie Schlëssel',
          
        }
      }
    }
  });

export default i18n;
