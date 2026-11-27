import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const BedrockCompanionApp());
}

const _purple = Color(0xFF9B7BFF);
const _background = Color(0xFF100D18);
const _surface = Color(0xFF1B1726);
const _muted = Color(0xFFAAA3B8);
const _channel = MethodChannel('bedrockpp/structures');

class BedrockCompanionApp extends StatelessWidget {
  const BedrockCompanionApp({super.key});

  @override
  Widget build(BuildContext context) {
    final scheme = ColorScheme.fromSeed(
      seedColor: _purple,
      brightness: Brightness.dark,
      surface: _surface,
    );
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Bedrock++ Companion',
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: scheme,
        scaffoldBackgroundColor: _background,
        appBarTheme: const AppBarTheme(
          backgroundColor: _background,
          foregroundColor: Colors.white,
          centerTitle: false,
          elevation: 0,
        ),
        cardTheme: CardThemeData(
          color: _surface,
          elevation: 0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(22),
            side: BorderSide(color: Colors.white.withValues(alpha: 0.07)),
          ),
        ),
      ),
      home: const CompanionHomePage(),
    );
  }
}

class CompanionHomePage extends StatefulWidget {
  const CompanionHomePage({super.key});

  @override
  State<CompanionHomePage> createState() => _CompanionHomePageState();
}

class _CompanionHomePageState extends State<CompanionHomePage> {
  Map<Object?, Object?>? _structure;
  bool _busy = false;
  String? _error;
  String? _success;

  Future<void> _importStructure() async {
    setState(() {
      _busy = true;
      _error = null;
      _success = null;
    });
    try {
      final result = await _channel.invokeMapMethod<Object?, Object?>('pickStructureData');
      if (!mounted) return;
      if (result != null) setState(() => _structure = result);
    } on PlatformException catch (e) {
      if (mounted) {
        setState(() {
          _structure = null;
          _error = e.message ?? 'Could not open that file.';
        });
      }
    } finally {
      if (mounted) setState(() => _busy = false);
    }
  }

  Future<void> _saveStructure() async {
    if (_structure == null) return;
    setState(() {
      _busy = true;
      _error = null;
      _success = null;
    });
    try {
      final result = await _channel.invokeMethod<String>('saveMcstructure');
      if (!mounted) return;
      if (result != null && result.isNotEmpty) setState(() => _success = result);
    } on PlatformException catch (e) {
      if (mounted) setState(() => _error = e.message ?? 'Could not save the structure.');
    } finally {
      if (mounted) setState(() => _busy = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final selected = _structure != null;
    return Scaffold(
      appBar: AppBar(
        title: Row(
          children: [
            Container(
              width: 36,
              height: 36,
              decoration: BoxDecoration(
                color: _purple.withValues(alpha: 0.16),
                borderRadius: BorderRadius.circular(11),
              ),
              child: const Icon(Icons.view_in_ar_rounded, color: _purple, size: 22),
            ),
            const SizedBox(width: 11),
            const Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Bedrock++', style: TextStyle(fontSize: 17, fontWeight: FontWeight.w800)),
                Text('COMPANION', style: TextStyle(fontSize: 9, letterSpacing: 2.2, color: _muted, fontWeight: FontWeight.w700)),
              ],
            ),
          ],
        ),
        actions: [
          IconButton(
            tooltip: 'About',
            onPressed: _showAbout,
            icon: const Icon(Icons.info_outline_rounded, color: _muted),
          ),
          const SizedBox(width: 6),
        ],
      ),
      body: SafeArea(
        child: ListView(
          padding: const EdgeInsets.fromLTRB(20, 12, 20, 32),
          children: [
            _hero(),
            const SizedBox(height: 22),
            const _SectionHeading(title: 'STRUCTURE WORKSPACE', trailing: 'TOOLS'),
            const SizedBox(height: 12),
            _workspaceCard(selected),
            if (selected) ...[
              const SizedBox(height: 14),
              _structureDetails(),
            ],
            if (_error != null) ...[
              const SizedBox(height: 12),
              _MessageCard(message: _error!, isError: true),
            ],
            if (_success != null) ...[
              const SizedBox(height: 12),
              _MessageCard(message: _success!, isError: false),
            ],
            const SizedBox(height: 24),
            const _SectionHeading(title: 'HOW IT WORKS'),
            const SizedBox(height: 12),
            const _StepTile(number: '01', icon: Icons.file_open_rounded, title: 'Import structure data', subtitle: 'Choose a .bppstruct.json file from your device.'),
            const _StepTile(number: '02', icon: Icons.fact_check_outlined, title: 'Validate the build', subtitle: 'Bedrock++ checks the dimensions, palette and block data.'),
            const _StepTile(number: '03', icon: Icons.save_alt_rounded, title: 'Save as .mcstructure', subtitle: 'Choose a destination in Android’s save-file dialog.'),
            const SizedBox(height: 22),
            Container(
              padding: const EdgeInsets.all(15),
              decoration: BoxDecoration(
                color: Colors.white.withValues(alpha: 0.035),
                borderRadius: BorderRadius.circular(16),
                border: Border.all(color: Colors.white.withValues(alpha: 0.06)),
              ),
              child: const Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Icon(Icons.info_outline_rounded, color: _muted, size: 19),
                  SizedBox(width: 10),
                  Expanded(
                    child: Text(
                      'The converter currently handles block palettes and block positions. Entity and block-entity data are not included yet.',
                      style: TextStyle(color: _muted, height: 1.45, fontSize: 12),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _hero() {
    return Container(
      padding: const EdgeInsets.fromLTRB(21, 23, 21, 21),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(25),
        gradient: const LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [Color(0xFF38265E), Color(0xFF211A35), Color(0xFF171322)],
        ),
        border: Border.all(color: _purple.withValues(alpha: 0.28)),
      ),
      child: Stack(
        children: [
          Positioned(
            right: -8,
            top: 0,
            child: Icon(Icons.view_in_ar_rounded, size: 112, color: Colors.white.withValues(alpha: 0.055)),
          ),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
                decoration: BoxDecoration(
                  color: _purple.withValues(alpha: 0.18),
                  borderRadius: BorderRadius.circular(30),
                ),
                child: const Text('MINECRAFT BEDROCK TOOLS', style: TextStyle(color: Color(0xFFD6C8FF), fontSize: 9, fontWeight: FontWeight.w800, letterSpacing: 1.2)),
              ),
              const SizedBox(height: 17),
              const Text('Your builds.\nYour files.', style: TextStyle(fontSize: 30, height: 1.08, fontWeight: FontWeight.w900, letterSpacing: -0.7)),
              const SizedBox(height: 10),
              const Text('A companion workspace for Bedrock++ structures and creative tools.', style: TextStyle(color: Color(0xFFC5BDD5), fontSize: 13, height: 1.45)),
              const SizedBox(height: 19),
              const Row(
                children: [
                  Icon(Icons.bolt_rounded, color: Color(0xFFCBB9FF), size: 16),
                  SizedBox(width: 5),
                  Text('STRUCTURE CONVERTER', style: TextStyle(color: Color(0xFFE3D9FF), fontSize: 10, fontWeight: FontWeight.w800, letterSpacing: 1)),
                ],
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _workspaceCard(bool selected) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(18),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Container(
                  width: 42,
                  height: 42,
                  decoration: BoxDecoration(color: _purple.withValues(alpha: 0.14), borderRadius: BorderRadius.circular(13)),
                  child: const Icon(Icons.architecture_rounded, color: _purple),
                ),
                const SizedBox(width: 12),
                const Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('Structure converter', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w800)),
                      SizedBox(height: 3),
                      Text('BPP data  →  Minecraft structure', style: TextStyle(color: _muted, fontSize: 11)),
                    ],
                  ),
                ),
                _StatusPill(label: selected ? 'READY' : 'IDLE', ready: selected),
              ],
            ),
            const SizedBox(height: 18),
            SizedBox(
              width: double.infinity,
              child: OutlinedButton.icon(
                onPressed: _busy ? null : _importStructure,
                icon: const Icon(Icons.folder_open_rounded, size: 19),
                label: const Text('Import structure data'),
                style: OutlinedButton.styleFrom(
                  foregroundColor: Colors.white,
                  side: BorderSide(color: _purple.withValues(alpha: 0.55)),
                  padding: const EdgeInsets.symmetric(vertical: 15),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(13)),
                ),
              ),
            ),
            const SizedBox(height: 10),
            SizedBox(
              width: double.infinity,
              child: FilledButton.icon(
                onPressed: (_busy || !selected) ? null : _saveStructure,
                icon: _busy
                    ? const SizedBox(width: 17, height: 17, child: CircularProgressIndicator(strokeWidth: 2))
                    : const Icon(Icons.save_alt_rounded, size: 19),
                label: Text(_busy ? 'Working…' : 'Create .mcstructure file'),
                style: FilledButton.styleFrom(
                  backgroundColor: _purple,
                  foregroundColor: const Color(0xFF160F25),
                  disabledBackgroundColor: Colors.white.withValues(alpha: 0.07),
                  disabledForegroundColor: _muted,
                  padding: const EdgeInsets.symmetric(vertical: 15),
                  textStyle: const TextStyle(fontWeight: FontWeight.w800),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(13)),
                ),
              ),
            ),
            if (!selected) ...[
              const SizedBox(height: 11),
              const Text('Import a Bedrock++ structure-data file to enable export.', style: TextStyle(color: _muted, fontSize: 11)),
            ],
          ],
        ),
      ),
    );
  }

  Widget _structureDetails() {
    final structure = _structure!;
    final size = (structure['size'] as List?)?.join(' × ') ?? 'Unknown';
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(17),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('SELECTED STRUCTURE', style: TextStyle(color: _muted, fontSize: 10, letterSpacing: 1.4, fontWeight: FontWeight.w800)),
            const SizedBox(height: 12),
            Text((structure['name'] ?? 'Unnamed structure').toString(), style: const TextStyle(fontSize: 17, fontWeight: FontWeight.w800)),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(child: _DetailChip(label: 'DIMENSIONS', value: size)),
                const SizedBox(width: 9),
                Expanded(child: _DetailChip(label: 'BLOCKS', value: (structure['blockCount'] ?? '—').toString())),
              ],
            ),
            const SizedBox(height: 10),
            _DetailChip(label: 'PALETTE ENTRIES', value: (structure['paletteCount'] ?? '—').toString()),
          ],
        ),
      ),
    );
  }

  void _showAbout() {
    showAboutDialog(
      context: context,
      applicationName: 'Bedrock++ Companion',
      applicationVersion: '0.1.0',
      applicationLegalese: 'An independent companion for Minecraft Bedrock creators.',
      children: const [
        SizedBox(height: 8),
        Text('Structure tools are in early development. Some workflows still require a supported transfer bridge from the in-game add-on.'),
      ],
    );
  }
}

class _SectionHeading extends StatelessWidget {
  const _SectionHeading({required this.title, this.trailing});
  final String title;
  final String? trailing;

  @override
  Widget build(BuildContext context) => Row(
        children: [
          Text(title, style: const TextStyle(color: _muted, fontSize: 10, fontWeight: FontWeight.w800, letterSpacing: 1.5)),
          const Spacer(),
          if (trailing != null) Text(trailing!, style: const TextStyle(color: _purple, fontSize: 9, fontWeight: FontWeight.w800, letterSpacing: 1.2)),
        ],
      );
}

class _StatusPill extends StatelessWidget {
  const _StatusPill({required this.label, required this.ready});
  final String label;
  final bool ready;

  @override
  Widget build(BuildContext context) => Container(
        padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 5),
        decoration: BoxDecoration(
          color: (ready ? const Color(0xFF69D6A2) : _muted).withValues(alpha: 0.12),
          borderRadius: BorderRadius.circular(30),
        ),
        child: Text(label, style: TextStyle(color: ready ? const Color(0xFF81E2B5) : _muted, fontSize: 9, fontWeight: FontWeight.w900, letterSpacing: 0.8)),
      );
}

class _StepTile extends StatelessWidget {
  const _StepTile({required this.number, required this.icon, required this.title, required this.subtitle});
  final String number;
  final IconData icon;
  final String title;
  final String subtitle;

  @override
  Widget build(BuildContext context) => Padding(
        padding: const EdgeInsets.only(bottom: 10),
        child: Row(
          children: [
            SizedBox(width: 27, child: Text(number, style: const TextStyle(color: _purple, fontSize: 11, fontWeight: FontWeight.w900))),
            Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.045), borderRadius: BorderRadius.circular(12)),
              child: Icon(icon, color: const Color(0xFFD0C5E8), size: 20),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(title, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.w700)),
                  const SizedBox(height: 3),
                  Text(subtitle, style: const TextStyle(color: _muted, fontSize: 11, height: 1.3)),
                ],
              ),
            ),
          ],
        ),
      );
}

class _DetailChip extends StatelessWidget {
  const _DetailChip({required this.label, required this.value});
  final String label;
  final String value;

  @override
  Widget build(BuildContext context) => Container(
        width: double.infinity,
        padding: const EdgeInsets.all(11),
        decoration: BoxDecoration(color: Colors.white.withValues(alpha: 0.035), borderRadius: BorderRadius.circular(12)),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(label, style: const TextStyle(color: _muted, fontSize: 8, fontWeight: FontWeight.w800, letterSpacing: 1)),
            const SizedBox(height: 5),
            Text(value, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.w700)),
          ],
        ),
      );
}

class _MessageCard extends StatelessWidget {
  const _MessageCard({required this.message, required this.isError});
  final String message;
  final bool isError;

  @override
  Widget build(BuildContext context) => Container(
        padding: const EdgeInsets.all(13),
        decoration: BoxDecoration(
          color: (isError ? const Color(0xFFFF7272) : const Color(0xFF69D6A2)).withValues(alpha: 0.10),
          borderRadius: BorderRadius.circular(13),
          border: Border.all(color: (isError ? const Color(0xFFFF7272) : const Color(0xFF69D6A2)).withValues(alpha: 0.25)),
        ),
        child: Row(
          children: [
            Icon(isError ? Icons.error_outline_rounded : Icons.check_circle_outline_rounded, color: isError ? const Color(0xFFFF9292) : const Color(0xFF81E2B5), size: 19),
            const SizedBox(width: 9),
            Expanded(child: Text(message, style: const TextStyle(fontSize: 12, height: 1.4))),
          ],
        ),
      );
}
