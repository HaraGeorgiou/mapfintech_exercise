<?php
// Fetch JSON live from Java REST API
$jsonData = file_get_contents("http://localhost:4567/sales");
$salesData = json_decode($jsonData, true);

// Collect unique products and continents
$products = [];
$continents = [];
foreach ($salesData as $row) {
    if (!in_array($row['product'], $products)) {
        $products[] = $row['product'];
    }
    if (!in_array($row['continent'], $continents)) {
        $continents[] = $row['continent'];
    }
}

// Handle selection
$selectedType = $_POST['type'] ?? 'product';
$selectedValue = $_POST['value'] ?? 'all';

// Initialize chart buckets
$chartData = ["Q1" => [], "Q2" => [], "Q3" => [], "Q4" => []];

// Filter based on dropdown
foreach ($salesData as $row) {
    if ($selectedValue === 'all' || $row[$selectedType] === $selectedValue) {
        $chartData[$row['quarter']][] = $row['sales_amount'];
    }
}

// Compute average for each quarter
$averageData = [];
foreach ($chartData as $quarter => $values) {
    $avg = !empty($values) ? array_sum($values) / count($values) : 0;
    $averageData[$quarter] = $avg;
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sales Data Dashboard</title>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['Quarter', 'Average Sales'],
                <?php
                foreach ($averageData as $quarter => $avg) {
                    echo "['$quarter', $avg],";
                }
                ?>
            ]);

            var options = {
                title: 'Average Sales per Quarter',
                curveType: 'function',
                legend: { position: 'bottom' }
            };

            var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
            chart.draw(data, options);
        }
    </script>
</head>
<body style="font-family: Arial; margin: 30px;">
    <h1>Sales Data Dashboard</h1>

    <form method="post">
        <label for="type">Choose filter type:</label>
        <select name="type" id="type" onchange="this.form.submit()">
            <option value="product" <?= ($selectedType === 'product') ? 'selected' : '' ?>>Product</option>
            <option value="continent" <?= ($selectedType === 'continent') ? 'selected' : '' ?>>Continent</option>
        </select>

        <label for="value">Select value:</label>
        <select name="value" id="value">
            <option value="all" <?= ($selectedValue === 'all') ? 'selected' : '' ?>>All</option>
            <?php
            // show either products or continents based on selected type
            $list = ($selectedType === 'continent') ? $continents : $products;
            foreach ($list as $item) {
                $sel = ($selectedValue === $item) ? 'selected' : '';
                echo "<option value='$item' $sel>$item</option>";
            }
            ?>
        </select>

        <button type="submit">Show Chart</button>
    </form>

    <div id="curve_chart" style="width: 900px; height: 500px; margin-top: 20px;"></div>
</body>
</html>
